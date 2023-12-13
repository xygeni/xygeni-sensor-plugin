package io.jenkins.plugins.xygeni.saltbuildstep;

import static org.junit.jupiter.api.Assertions.*;

import hudson.model.Run;
import hudson.model.TaskListener;
import java.util.List;

import io.jenkins.plugins.xygeni.saltcommand.XygeniSaltAtSlsaCommandBuilder;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class XygeniSaltSlsaCommandTest {

    @Test
    void run() {

        Run mockRun = Mockito.mock(Run.class);
        Mockito.when(mockRun.getFullDisplayName()).thenReturn("disp-name");

        TaskListener mockTList = Mockito.mock(TaskListener.class);
        Mockito.when(mockTList.getLogger()).thenReturn(System.out);

        System.out.println(new XygeniSaltAtSlsaCommandBuilder("key",
                                                              "pass",
                                                              "publicKey",
                                                              "format",
                                                              "cert",
                                                              List.of())
                               .withRun(mockRun, null , mockTList)
                               .withAttestationOptions(true, "p", false)
                               .withOutputOptions("out", true, null)
                               .build().getCommandArgs());

    }
}
