package io.jenkins.plugins.xygeni.saltcommand;

import hudson.Launcher;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.util.ArgumentListBuilder;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

public class XygeniSaltAtCommand {

    private Run<?, ?> build;
    private Launcher launcher;
    private TaskListener listener;

    private ArgumentListBuilder args;

    public void run() {

        PrintStream print_stream = null;
        try {

            Launcher.ProcStarter ps = launcher.launch();
            ps.cmds(getCommandArgs());
            ps.stdin(null);
            ps.stderr(listener.getLogger());

            String outFileName = "salt-command.out";
            File outFile = new File(build.getRootDir(), outFileName);
            print_stream = new PrintStream(outFile, StandardCharsets.UTF_8);
            ps.stdout(listener.getLogger());
            ps.quiet(true);

            listener.getLogger().println("Salt command in progress...");
            listener.getLogger().println("" + args.toString());
            ps.join(); // RUN !

        } catch (IOException | InterruptedException e) {
            listener.getLogger().println("Error running Xygeni Salt:" + e.getMessage());
        } finally {
            if (print_stream != null) {
                print_stream.close();
            }
        }
    }

    public ArgumentListBuilder getCommandArgs() {
        return args;
    }

    public void setBuild(Run<?, ?> build) {
        this.build = build;
    }

    public void setLauncher(Launcher launcher) {
        this.launcher = launcher;
    }

    public void setListener(TaskListener listener) {
        this.listener = listener;
    }

    public void setArgs(ArgumentListBuilder args) {
        this.args = args;
    }
}
