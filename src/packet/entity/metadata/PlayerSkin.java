package lt.wayout.minecraft.plugin.wayengine.packet.entity.metadata;

import com.google.common.base.Preconditions;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerSkin {
    private Boolean cape, jacket, leftSleeve, rightSleeve, leftPants, rightPants, hat;
    private final String texture, signature;

    public PlayerSkin(@NotNull String texture, @NotNull String signature) {
        this.texture = Preconditions.checkNotNull(texture, "String texture object cannot be null!");
        this.signature = Preconditions.checkNotNull(signature, "String signature object cannot be null!");
        this.cape = true;
        this.leftSleeve = true;
        this.rightSleeve = true;
        this.leftPants = true;
        this.rightPants = true;
        this.hat = true;
    }

    public @Nullable Boolean getCapeState() {
        return this.cape;
    }

    public void setSaddleState(Boolean state) {
        this.cape = state;
    }

    public @Nullable Boolean getJacketState() {
        return this.jacket;
    }

    public void setJacketState(Boolean state) {
        this.cape = state;
    }

    public @Nullable Boolean getLeftSleeveState() {
        return this.leftSleeve;
    }

    public void setLeftSleeveState(Boolean state) {
        this.leftSleeve = state;
    }

    public @Nullable Boolean getRightSleeveState() {
        return this.rightSleeve;
    }

    public void setRightSleeveState(Boolean state) {
        this.rightSleeve = state;
    }

    public @Nullable Boolean getLeftPantsState() {
        return this.leftPants;
    }

    public void setLeftPantsState(Boolean state) {
        this.leftPants = state;
    }

    public @Nullable Boolean getRightPantsState() {
        return this.rightPants;
    }

    public void setRightPantsState(Boolean state) {
        this.rightPants = state;
    }

    public @Nullable Boolean getHatState() {
        return this.hat;
    }

    public void setHatState(Boolean state) {
        this.hat = state;
    }

    @Nullable
    public String getTexture() {
        return this.texture;
    }

    @Nullable
    public String getSignature() {
        return this.signature;
    }
}
