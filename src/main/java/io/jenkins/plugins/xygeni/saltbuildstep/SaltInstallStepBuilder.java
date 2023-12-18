package io.jenkins.plugins.xygeni.saltbuildstep;

import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.Extension;
import hudson.ExtensionList;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.AbstractProject;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Builder;
import io.jenkins.plugins.xygeni.saltcommand.XygeniSaltInstallBuilder;
import java.io.PrintStream;
import java.util.logging.Logger;
import jenkins.tasks.SimpleBuildStep;
import org.jenkinsci.Symbol;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;

/**
 * Salt Install Step Builder Class.
 * <p>
 *
 *
 * @author Victor de la Rosa
 */
public class SaltInstallStepBuilder extends Builder implements SimpleBuildStep {

    private static final Logger logger = Logger.getLogger(SaltInstallStepBuilder.class.getName());

    private String operatingSystem;

    @DataBoundSetter
    public void setOperatingSystem(String operatingSystem) {
        this.operatingSystem = operatingSystem;
    }

    public String getOperatingSystem() {
        return operatingSystem;
    }

    @DataBoundConstructor
    public SaltInstallStepBuilder() {}

    @Override
    public void perform(
            @NonNull Run<?, ?> run,
            @NonNull FilePath workspace,
            @NonNull Launcher launcher,
            @NonNull TaskListener listener) {

        PrintStream console = listener.getLogger();

        console.println("[xygeniSalt] Installing ..");

        new XygeniSaltInstallBuilder(operatingSystem)
                .withRun(run, launcher, listener)
                .build()
                .run();
    }

    @Override
    public DescriptorImpl getDescriptor() {
        return (DescriptorImpl) super.getDescriptor();
    }

    /**
     * Descriptor for {@link SaltInstallStepBuilder}.
     */
    @Symbol("xygeniSaltInstall")
    @Extension
    public static final class DescriptorImpl extends BuildStepDescriptor<Builder> {

        // descritor

        public static DescriptorImpl get() {
            return ExtensionList.lookupSingleton(DescriptorImpl.class);
        }

        public DescriptorImpl() {
            load();
        }

        @NonNull
        public String getDisplayName() {
            return "Xygeni Salt Attestation 'Install' command";
        }

        @Override
        public boolean isApplicable(Class<? extends AbstractProject> jobType) {
            return true;
        }
    }
}
