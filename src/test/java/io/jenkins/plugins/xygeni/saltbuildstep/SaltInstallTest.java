package io.jenkins.plugins.xygeni.saltbuildstep;

import hudson.EnvVars;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.Result;
import hudson.model.Run;
import hudson.model.TaskListener;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mockito;

class SaltInstallTest {

    @Test
    void runSaltInstall(@TempDir File tempDir) throws IOException, InterruptedException {

        String expected = "salt at --never-fail init --pipeline=MyPipeline --basedir=" + tempDir.getPath()
                + " --attestor=git --attestor=env --exclude=**/*Test* --materials=src/";

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(baos);

        runInstall(tempDir, printStream);

        assertLogContains(expected, baos);
    }

    private void assertLogContains(String expected, ByteArrayOutputStream baos) {
        String output = baos.toString();
        Assertions.assertTrue(
                output.contains(expected), "Expected: \n" + expected + "\nnot found in: \n" + baos.toString());
    }

    private void runInstall(File tempDir, PrintStream printStream) throws IOException, InterruptedException {

        Run mockRun = Mockito.mock(Run.class);
        Mockito.when(mockRun.getResult()).thenReturn(Result.SUCCESS);
        Mockito.when(mockRun.getRootDir()).thenReturn(tempDir);
        Mockito.when(mockRun.getFullDisplayName()).thenReturn("MyPipeline");

        TaskListener mockTList = Mockito.mock(TaskListener.class);
        Mockito.when(mockTList.getLogger()).thenReturn(printStream);

        EnvVars mockEVars = Mockito.mock(EnvVars.class);
        Mockito.when(mockEVars.expand(Mockito.anyString())).thenReturn("");

        FilePath mockWorkspace = Mockito.mock(FilePath.class);
        Mockito.when(mockWorkspace.list(Mockito.anyString())).thenReturn(new FilePath[] {new FilePath(tempDir)});

        SaltInstallStepBuilder p = new SaltInstallStepBuilder();
        p.perform(mockRun, mockWorkspace, mockEVars, new Launcher.DummyLauncher(mockTList), mockTList);
    }
}
