package info.palinc;

import hudson.FilePath;
import hudson.model.Run;
import hudson.model.TaskListener;
import java.io.File;
import java.io.IOException;
import java.lang.Exception;
import java.lang.InterruptedException;
import java.lang.Override;
import java.lang.String;
import java.lang.System;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import javax.annotation.Nonnull;
import org.jenkinsci.plugins.workflow.libs.LibraryRetriever;

/**
 * Retriever used for retrieving local pipeline shared library implementation. */
public final class LocalLibraryRetriever extends LibraryRetriever {
    private final File localDirectory;

    public LocalLibraryRetriever() {
        this(Paths.get(System.getProperty("user.dir")));
    }

    public LocalLibraryRetriever(final Path path) {
        localDirectory = Objects.requireNonNull(path).toFile();
    }

    @Override
    public final void retrieve(final @Nonnull String name,
                               final @Nonnull String version,
                               final boolean changelog,
                               final @Nonnull FilePath target,
                               @Nonnull final Run<?, ?> run,
                               final @Nonnull TaskListener listener) throws Exception {
        doRetrieve(target, listener);
    }

    @Override
    public final void retrieve(final @Nonnull String name,
                               final @Nonnull String version,
                               final @Nonnull FilePath target,
                               @Nonnull final Run<?, ?> run,
                               final @Nonnull TaskListener listener) throws Exception {
        doRetrieve(target, listener);
    }

    private void doRetrieve(final @Nonnull FilePath target, final @Nonnull TaskListener listener)
            throws IOException, InterruptedException {
        final FilePath localFilePath = new FilePath(localDirectory);
        listener.getLogger().format("Copying from local path %s to workspace path %s%s", localDirectory, target, System.lineSeparator());
        // Exclusion filter copied from SCMSourceRetriever
        localFilePath.copyRecursiveTo("src/**/*.groovy,vars/*.groovy,vars/*.txt,resources/", null, target);
    }
}
