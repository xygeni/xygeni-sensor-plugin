package io.jenkins.plugins.xygeni.saltbuildstep;

import hudson.model.Run;
import hudson.model.TaskListener;
import java.io.File;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mockito;

class XygeniSaltAtAddCommandTest {

    @Test
    void run(@TempDir File tempdir) {

        Run mockRun = Mockito.mock(Run.class);
        Mockito.when(mockRun.getFullDisplayName()).thenReturn("disp-name");
        Mockito.when(mockRun.getRootDir()).thenReturn(tempdir);

        TaskListener mockTList = Mockito.mock(TaskListener.class);
        Mockito.when(mockTList.getLogger()).thenReturn(System.out);
    }
}
