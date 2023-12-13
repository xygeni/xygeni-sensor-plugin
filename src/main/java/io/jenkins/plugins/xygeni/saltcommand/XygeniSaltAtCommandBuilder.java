package io.jenkins.plugins.xygeni.saltcommand;

import hudson.Launcher;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.util.ArgumentListBuilder;

public abstract class XygeniSaltAtCommandBuilder {

    private ArgumentListBuilder args;

    private Run<?, ?> build;
    private Launcher launcher;
    private TaskListener listener;
    private String output;
    private boolean prettyPrint = false;
    private String outputUnsigned;
    private boolean noUpload;
    private String project;
    private boolean noResultUpload;

    XygeniSaltAtCommandBuilder() {
        this.args = new ArgumentListBuilder();
    }

    public XygeniSaltAtCommandBuilder withRun(Run<?, ?> build, Launcher launcher, TaskListener listener) {
        this.build = build;
        this.launcher = launcher;
        this.listener = listener;
        return this;
    }

    public XygeniSaltAtCommandBuilder withAttestationOptions(boolean noUpload, String project, boolean noResultUpload) {
        this.noUpload = noUpload;
        this.project = project;
        this.noResultUpload = noResultUpload;
        return this;
    }

    public XygeniSaltAtCommandBuilder withOutputOptions(String output, boolean prettyPrint, String outputUnsigned) {
        this.output = output;
        this.prettyPrint = prettyPrint;
        this.outputUnsigned = outputUnsigned;
        return this;
    }

    protected abstract String getCommand();

    protected abstract void addCommandArgs(ArgumentListBuilder args, Run<?, ?> build);

    public XygeniSaltAtCommand build() {

        args.add("salt", "at", "--never-fail", getCommand()); // provenance slsa attestation command
        args.add("--pipeline", build.getFullDisplayName());

        if (noUpload) {
            args.add("--no-upload");
        }
        if (project != null && !project.isEmpty()) {
            args.add("--project", project);
        }
        if (noResultUpload) {
            args.add("--no-result-upload");
        }
        if (output != null && !output.isEmpty()) {
            args.add("-o", output);
        }
        if (prettyPrint) {
            args.add("--pretty-print");
        }
        if (outputUnsigned != null && !outputUnsigned.isEmpty()) {
            args.add("--outputUnsigned", outputUnsigned);
        }

        addCommandArgs(args, build);

        XygeniSaltAtCommand command = new XygeniSaltAtCommand();
        command.setBuild(build);
        command.setLauncher(launcher);
        command.setListener(listener);
        command.setArgs(args);
        return command;
    }
}
