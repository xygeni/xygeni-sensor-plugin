package io.jenkins.plugins.xygeni.saltbuildstep;

import hudson.EnvVars;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.Result;
import hudson.model.Run;
import hudson.model.TaskListener;
import io.jenkins.plugins.xygeni.saltbuildstep.model.Item;
import io.jenkins.plugins.xygeni.saltbuildstep.model.OutputOptions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

class SaltAtRunTest {

    @Test
    void perform(@TempDir File tempDir) throws IOException, InterruptedException {

        Run mockRun = Mockito.mock(Run.class);
        Mockito.when(mockRun.getResult()).thenReturn(Result.SUCCESS);
        Mockito.when(mockRun.getRootDir()).thenReturn(tempDir);

        TaskListener mockTList = Mockito.mock(TaskListener.class);
        Mockito.when(mockTList.getLogger()).thenReturn(System.out);

        EnvVars mockEVars = Mockito.mock(EnvVars.class);
        Mockito.when(mockEVars.expand(Mockito.anyString())).thenReturn("");

        FilePath mockWorkspace = Mockito.mock(FilePath.class);
        Mockito.when(mockWorkspace.list(Mockito.anyString())).thenReturn(new FilePath[] {new FilePath(tempDir)});

        SaltAtRunStepBuilder p = new SaltAtRunStepBuilder();
        p.setItems(List.of(new Item("name", "product", null, "value", "", "", "")));
        p.setMaxout(10);
        p.setStep("step");
        p.setMaxerr(10);
        p.setTimeout(10);
        p.setCommand("build");
        p.setItems(List.of(new Item("name", "product", null, "value", "", "", "")));

        p.setOutputOptions(new OutputOptions("out", true, null));
        p.perform(mockRun, mockWorkspace, mockEVars, new Launcher.DummyLauncher(mockTList), mockTList);
    }

    @Test
    void runSaltRun(@TempDir File tempDir) throws IOException, InterruptedException {

        String expected = "salt at --never-fail run --pipeline MyPipeline -o out.json --pretty-print --max-out 10 --step my-step --max-err 9 --timeout 11 -n item-name -t product --digest=sha256:abcde -- \"run build\"";

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(baos);

        runAdd(10, 9, "my-step", 11, "run build", List.of(new Item("item-name", "product", null, "", "", "", "sha256:abcde"))
            ,tempDir ,printStream);

        assertLogContains(expected, baos);
    }


    private void assertLogContains(String expected, ByteArrayOutputStream baos){
        String output = baos.toString();
        Assertions.assertTrue(output.contains(expected), "Expected: \n" + expected + "\nnot found in: \n" + baos.toString());
    }

    private void runAdd(int maxout, int maxerr, String step, int timeout, String commandline, List<Item> items, File tempDir, PrintStream printStream) throws IOException, InterruptedException {

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

        SaltAtRunStepBuilder p = new SaltAtRunStepBuilder();
        p.setItems(items);
        p.setMaxout(maxout);
        p.setStep(step);
        p.setMaxerr(maxerr);
        p.setTimeout(timeout);
        p.setCommand(commandline);
        p.setOutputOptions(new OutputOptions("out.json", true, null));
        p.perform(mockRun, mockWorkspace, mockEVars, new Launcher.DummyLauncher(mockTList), mockTList);
    }
}
