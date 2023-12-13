package io.jenkins.plugins.xygeni.saltcommand;

import hudson.model.Run;
import hudson.util.ArgumentListBuilder;
import io.jenkins.plugins.xygeni.saltbuildstep.model.Material;
import java.util.List;

public class XygeniSaltAtInitCommandBuilder extends XygeniSaltAtCommandBuilder {

    private static final String INIT_COMMAND = "init";

    private final List<String> attestors;
    private final String exclude;
    private final List<Material> materials;

    public XygeniSaltAtInitCommandBuilder(List<String> attestors, String exclude, List<Material> materials) {
        this.attestors = attestors;
        this.exclude = exclude;
        this.materials = materials;
    }

    @Override
    protected String getCommand() {
        return INIT_COMMAND;
    }

    @Override
    protected void addCommandArgs(ArgumentListBuilder args, Run<?, ?> build) {

        args.add("--basedir", build.getRootDir().getPath());
        for (String attestor : attestors) {
            args.add("--attestor", attestor);
        }
        args.add("--exclude", exclude);

        for (Material material : materials) {
            args.add("--materials", material.getMaterial());
        }
    }
}
