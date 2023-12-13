package io.jenkins.plugins.xygeni.saltbuildstep;

import hudson.model.Run;
import hudson.model.TaskListener;
import io.jenkins.plugins.xygeni.saltbuildstep.model.Item;
import io.jenkins.plugins.xygeni.saltcommand.XygeniSaltAtRunCommandBuilder;
import java.io.File;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mockito;

class XygeniSaltAtRunCommandTest {

    @Test
    void run(@TempDir File tempdir) {

        Run mockRun = Mockito.mock(Run.class);
        Mockito.when(mockRun.getFullDisplayName()).thenReturn("disp-name");
        Mockito.when(mockRun.getRootDir()).thenReturn(tempdir);

        TaskListener mockTList = Mockito.mock(TaskListener.class);
        Mockito.when(mockTList.getLogger()).thenReturn(System.out);

        System.out.println(new XygeniSaltAtRunCommandBuilder(
                        10, "step", 10, 10, List.of(new Item("name", "product", null, "value", null, "", "")), "build")
                .withRun(mockRun, null, mockTList)
                .withOutputOptions("out.json", true, null)
                .build()
                .getCommandArgs());
    }
}
