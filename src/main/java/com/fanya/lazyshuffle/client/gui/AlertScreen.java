package com.fanya.lazyshuffle.client.gui;

import com.fanya.lazyshuffle.client.LazyshuffleClient;
import com.fanya.lazyshuffle.client.util.FileManager;
import com.fanya.lazyshuffle.client.util.PresetGenerator;
import dev.lambdaurora.spruceui.Position;
import dev.lambdaurora.spruceui.navigation.NavigationDirection;
import dev.lambdaurora.spruceui.render.SpruceGuiGraphics;
import dev.lambdaurora.spruceui.screen.SpruceScreen;
import dev.lambdaurora.spruceui.widget.SpruceButtonWidget;
import dev.lambdaurora.spruceui.widget.container.SpruceContainerWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ConfirmLinkScreen;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.OrderedText;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import static com.fanya.lazyshuffle.client.LazyshuffleClient.CONFIG_DIR;

public class AlertScreen extends SpruceScreen {
    public static Screen currentScreen;
    private final Screen parent;
    private ScrollableTextContainer textContainer;

    public AlertScreen(Screen parent) {
        super(Text.translatable("screen.lazyshuffle.title"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        super.init();

        int containerHeight = this.height - 60;
        textContainer = new ScrollableTextContainer(
                Position.of(0, 20),
                this.width,
                containerHeight
        );
        this.addDrawableChild(textContainer);

        this.addDrawableChild(new SpruceButtonWidget(
                Position.of(this.width / 2 - 128 - 5, this.height - 23),
                128, 20,
                ScreenTexts.CONTINUE,
                button -> {
                    try {
                        FileManager.createBackup(FileManager.getPresetsFile(CONFIG_DIR), FileManager.getBackupDir(CONFIG_DIR));
                        LazyshuffleClient.LOGGER.info("Backup created successfully.");

                        PresetGenerator.generatePresets(FileManager.getLazyShuffleDir(CONFIG_DIR), FileManager.getPresetsFile(CONFIG_DIR));
                        LazyshuffleClient.LOGGER.info("New presets.json generated successfully.");

                        reloadSkinShufflePresets();
                        LazyshuffleClient.LOGGER.info("SkinShuffle presets reloaded successfully.");

                        this.client.setScreen(currentScreen);
                        MinecraftClient.getInstance().getToastManager().add(new SystemToast(
                                SystemToast.Type.PERIODIC_NOTIFICATION,
                                Text.translatable("toast.lazyshuffle.title"),
                                Text.translatable("toast.lazyshuffle.message")
                        ));
                    } catch (Exception e) {
                        LazyshuffleClient.LOGGER.error("Error during preset generation or reload: {}", e.getMessage());
                        e.printStackTrace();
                    }
                }
        ));

        this.addDrawableChild(new SpruceButtonWidget(
                Position.of(this.width / 2 + 5, this.height - 23),
                128, 20,
                ScreenTexts.BACK,
                button -> this.client.setScreen(currentScreen)
        ));
    }

    @Override
    public void render(@NotNull SpruceGuiGraphics graphics, int mouseX, int mouseY, float delta) {
        super.render(graphics, mouseX, mouseY, delta);

        graphics.vanilla().drawTextWithShadow(
                this.client.textRenderer,
                this.title,
                this.width / 2 - this.client.textRenderer.getWidth(this.title) / 2,
                10,
                0xFFFFFF
        );
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

    @Override
    public void close() {
        this.client.setScreen(parent);
    }

    public static void reloadSkinShufflePresets() {
        try {
            Class<?> skinPresetManagerClass = Class.forName("dev.imb11.skinshuffle.client.config.SkinPresetManager");

            Field loadedPresetsField = skinPresetManagerClass.getDeclaredField("loadedPresets");
            loadedPresetsField.setAccessible(true);

            List<?> loadedPresets = (List<?>) loadedPresetsField.get(null);
            loadedPresets.clear();

            Method loadPresetsMethod = skinPresetManagerClass.getDeclaredMethod("loadPresets");
            loadPresetsMethod.invoke(null);

        } catch (Exception e) {
            LazyshuffleClient.LOGGER.error("ERROR: ", e);
        }
    }

    private static class ScrollableTextContainer extends SpruceContainerWidget {
        private double scrollOffset = 0;
        private double maxScrollOffset = 0;
        private final Text[] lines;

        public ScrollableTextContainer(Position position, int width, int height) {
            super(position, width, height);

            this.lines = new Text[] {
                    Text.translatable("screen.lazyshuffle.text"),
                    Text.translatable("screen.lazyshuffle.instruction.skin_folder"),
                    Text.translatable("screen.lazyshuffle.instruction.model"),
                    Text.translatable("screen.lazyshuffle.instruction.model_example"),
                    Text.translatable("screen.lazyshuffle.instruction.final"),
                    Text.translatable("screen.lazyshuffle.instruction.backup"),
                    Text.translatable("screen.lazyshuffle.instruction.backup_two"),
                    Text.translatable("screen.lazyshuffle.attention")
            };
        }

        @Override
        public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
            scrollOffset = Math.max(0, Math.min(scrollOffset - scrollY * 10, maxScrollOffset));
            return true;
        }

        @Override
        public void render(SpruceGuiGraphics context, int mouseX, int mouseY, float delta) {
            super.render(context, mouseX, mouseY, delta);

            context.enableScissor(
                    this.getX(),
                    this.getY(),
                    this.getX() + this.width,
                    this.getY() + this.height
            );

            int lineHeight = this.client.textRenderer.fontHeight + 5;
            int currentY = this.getY() + 5 - (int) scrollOffset;
            int wrapWidth = this.width - 20;
            int totalContentHeight = 0;

            for (Text line : lines) {
                if (line.getString().isEmpty()) {
                    totalContentHeight += lineHeight;
                } else {
                    var wrappedLines = this.client.textRenderer.wrapLines(line, wrapWidth);
                    totalContentHeight += wrappedLines.size() * lineHeight;
                }
            }

            maxScrollOffset = Math.max(0, totalContentHeight - this.height + 10);

            for (Text line : lines) {
                if (line.getString().isEmpty()) {
                    currentY += lineHeight;
                    continue;
                }
                var wrappedLines = this.client.textRenderer.wrapLines(line, wrapWidth);
                for (OrderedText wrappedLine : wrappedLines) {
                    context.vanilla().drawTextWithShadow(
                            this.client.textRenderer,
                            wrappedLine,
                            this.getX() + 10,
                            currentY,
                            0xFFFFFFFF
                    );
                    currentY += lineHeight;
                }
            }

            if (maxScrollOffset > 0) {
                int scrollbarHeight = (int) ((this.height * (double) this.height) / totalContentHeight);
                int scrollbarY = (int) (this.getY() + (scrollOffset / maxScrollOffset) * (this.height - scrollbarHeight));

                context.fill(
                        this.getX() + this.width - 5,
                        this.getY(),
                        this.getX() + this.width,
                        this.getY() + this.height,
                        0x80000000
                );
                context.fill(
                        this.getX() + this.width - 5,
                        scrollbarY,
                        this.getX() + this.width,
                        scrollbarY + scrollbarHeight,
                        0xFFFFFFFF
                );
            }

            context.disableScissor();
        }

        @Override
        public boolean onNavigation(NavigationDirection direction, boolean tab) {
            return super.onNavigation(direction, tab);
        }
    }
}