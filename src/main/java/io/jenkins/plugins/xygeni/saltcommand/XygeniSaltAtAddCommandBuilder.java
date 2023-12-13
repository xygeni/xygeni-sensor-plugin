package io.jenkins.plugins.xygeni.saltcommand;

import hudson.model.Run;
import hudson.util.ArgumentListBuilder;
import io.jenkins.plugins.xygeni.saltbuildstep.model.Item;

import java.util.List;

public class XygeniSaltAtAddCommandBuilder extends XygeniSaltAtCommandBuilder {

    private static final String ADD_COMMAND = "add";

    private final List<Item> items;

    public XygeniSaltAtAddCommandBuilder(List<Item> items) {
        this.items = items;
    }

    @Override
    protected String getCommand() {
        return ADD_COMMAND;
    }

    @Override
    protected void addCommandArgs(ArgumentListBuilder args, Run<?, ?> build) {

        args.add("--basedir", build.getRootDir().getPath());
        for (Item item : items) {
            args.add("-n", item.getName());
            args.add("-t", item.getType());
            if (item.getType().equals(Item.Type.predicate.name())) {
                args.add("--predicate-type=", item.getPredicateType());
            }
            if (item.isValue()) {
                args.add("-v", item.getValue());
            } else if (item.isFile()) {
                args.add("-f", item.getFile());
            } else if (item.isDigest()) {
                args.add("--digest=" + item.getDigest());
            } else {
                args.add("-i", item.getImage());
            }
        }
    }
}
