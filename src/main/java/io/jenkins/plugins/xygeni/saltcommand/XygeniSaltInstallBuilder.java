package io.jenkins.plugins.xygeni.saltcommand;

import hudson.Launcher;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.util.ArgumentListBuilder;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class XygeniSaltInstallBuilder {

    private ArgumentListBuilder args;

    private String operatingSystem;

    private Run<?, ?> build;
    private Launcher launcher;
    private TaskListener listener;

    public XygeniSaltInstallBuilder(String operatingSystem) {
        this.args = new ArgumentListBuilder();
        this.operatingSystem = operatingSystem;
    }

    public XygeniSaltInstallBuilder withRun(Run<?, ?> build, Launcher launcher, TaskListener listener) {
        this.build = build;
        this.launcher = launcher;
        this.listener = listener;
        return this;
    }

    public XygeniSaltAtCommand build() {

        String linux = "#!/bin/sh\n" + "# download zipfile\n"
                + "curl -sLO https://get.xygeni.io/latest/salt/salt.zip\n"
                + "echo verify integrity\n"
                + "echo $(curl -s https://raw.githubusercontent.com/xygeni/xygeni/main/checksum/latest/salt.zip.sha256) salt.zip | sha256sum --check\n"
                + "# unzip\n"
                + "unzip -o -q salt.zip -d .\n"
                + "# add to path\n"
                + "echo \"#!/bin/bash\n./xygeni_salt/salt $@\" > salt \n"
                + "chmod +x salt"
                + "salt --version\n";

        String windows = "";

        if ("linux".equals(operatingSystem)) {
            try {
                File scriptFile = File.createTempFile("installScript", ".sh", build.getRootDir());
                scriptFile.setExecutable(true);

                // Step 2: Write commands to the file
                try (FileWriter writer = new FileWriter(scriptFile)) {
                    writer.write(linux); // Omit this line for Windows batch files
                }

                args.add(scriptFile);
            } catch (IOException ex) {
                throw new UnsupportedOperationException("Cannot run install script.", ex);
            }
        } else if ("windows".equals(operatingSystem)) {
            args.add("iwr", "https://get.xygeni.io/latest/salt/salt.zip", "-useb", "-OutFile", "salt.zip", "-and");
            // verify integrity
            args.add(
                    "set",
                    "salt_digest",
                    "(iwr https://raw.githubusercontent.com/xygeni/xygeni/main/checksum/latest/salt.zip.sha256).Content",
                    "-and");
            args.add("(Get-FileHash '.\\salt.zip' -Algorithm SHA256).Hash", "-eq", "$salt_digest", "-and");
            // unzip
            args.add("Expand-Archive", "'.\\salt.zip'", "-DestionationPath", ".", "-and");
            // add alias
            args.add("Set-Alias", "-Name", "salt", "-Value", ".\\salt.ps1");
        } else {
            throw new UnsupportedOperationException(operatingSystem + " not supported");
        }

        XygeniSaltAtCommand command = new XygeniSaltAtCommand();
        command.setBuild(build);
        command.setLauncher(launcher);
        command.setListener(listener);
        command.setArgs(args);
        return command;
    }
}
