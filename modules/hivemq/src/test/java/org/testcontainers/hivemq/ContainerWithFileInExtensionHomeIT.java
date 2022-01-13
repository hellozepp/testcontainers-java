package org.testcontainers.hivemq;

import com.hivemq.extension.sdk.api.ExtensionMain;
import com.hivemq.extension.sdk.api.interceptor.publish.PublishInboundInterceptor;
import com.hivemq.extension.sdk.api.parameter.ExtensionStartInput;
import com.hivemq.extension.sdk.api.parameter.ExtensionStartOutput;
import com.hivemq.extension.sdk.api.parameter.ExtensionStopInput;
import com.hivemq.extension.sdk.api.parameter.ExtensionStopOutput;
import com.hivemq.extension.sdk.api.services.Services;
import com.hivemq.extension.sdk.api.services.intializer.ClientInitializer;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.testcontainers.hivemq.util.TestPublishModifiedUtil;
import org.testcontainers.utility.MountableFile;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

public class ContainerWithFileInExtensionHomeIT {


    @Test
    @Timeout(value = 3, unit = TimeUnit.MINUTES)
    void test() throws Exception {
        final HiveMQExtension hiveMQExtension = HiveMQExtension.builder()
                .id("extension-1")
                .name("my-extension")
                .version("1.0")
                .mainClass(FileCheckerExtension.class).build();

        final HiveMQContainer hivemq =
                new HiveMQContainer(HiveMQContainer.DEFAULT_HIVEMQ_CE_IMAGE_NAME)
                    .withHiveMQConfig(MountableFile.forClasspathResource("/inMemoryConfig.xml"))
                    .withExtension(hiveMQExtension)
                    .waitForExtension(hiveMQExtension)
                    .withFileInExtensionHomeFolder(
                        MountableFile.forClasspathResource("/additionalFile.txt"),
                        "extension-1",
                        "/additionalFiles/my-file.txt");

        hivemq.start();
        TestPublishModifiedUtil.testPublishModified(hivemq.getMqttPort());
        hivemq.stop();
    }

    public static class FileCheckerExtension implements ExtensionMain {

        @Override
        public void extensionStart(@NotNull ExtensionStartInput extensionStartInput, @NotNull ExtensionStartOutput extensionStartOutput) {

            final PublishInboundInterceptor publishInboundInterceptor = (publishInboundInput, publishInboundOutput) -> {

                final File extensionHomeFolder = extensionStartInput.getExtensionInformation().getExtensionHomeFolder();

                final File additionalFile = new File(extensionHomeFolder, "additionalFiles/my-file.txt");

                if (additionalFile.exists()) {
                    publishInboundOutput.getPublishPacket().setPayload(ByteBuffer.wrap("modified".getBytes(StandardCharsets.UTF_8)));
                }
            };

            final ClientInitializer clientInitializer = (initializerInput, clientContext) -> clientContext.addPublishInboundInterceptor(publishInboundInterceptor);

            Services.initializerRegistry().setClientInitializer(clientInitializer);
        }

        @Override
        public void extensionStop(@NotNull ExtensionStopInput extensionStopInput, @NotNull ExtensionStopOutput extensionStopOutput) {

        }
    }

}
