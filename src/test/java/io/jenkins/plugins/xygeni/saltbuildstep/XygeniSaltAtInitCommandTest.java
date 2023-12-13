package io.jenkins.plugins.xygeni.saltbuildstep;

import hudson.model.Run;
import hudson.model.TaskListener;
import io.jenkins.plugins.xygeni.saltbuildstep.model.Material;
import io.jenkins.plugins.xygeni.saltcommand.XygeniSaltAtInitCommandBuilder;
import java.io.File;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mockito;

class XygeniSaltAtInitCommandTest {

    @Test
    void run(@TempDir File tempdir) {

        Run mockRun = Mockito.mock(Run.class);
        Mockito.when(mockRun.getFullDisplayName()).thenReturn("disp-name");
        Mockito.when(mockRun.getRootDir()).thenReturn(tempdir);

        TaskListener mockTList = Mockito.mock(TaskListener.class);
        Mockito.when(mockTList.getLogger()).thenReturn(System.out);

        System.out.println(new XygeniSaltAtInitCommandBuilder(List.of("git"), "", List.of(new Material("src")))
                .withRun(mockRun, null, mockTList)
                .withAttestationOptions(true, "myproject", false)
                .withOutputOptions("out.json", true, null)
                .build()
                .getCommandArgs());
    }
}
