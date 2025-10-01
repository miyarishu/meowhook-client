package meow.miyarishu.meowhook.features.modules.visual;

import meow.miyarishu.meowhook.features.setting.Setting;

import meow.miyarishu.meowhook.features.modules.Module;

public class HitAnimations
        extends Module {
    private static HitAnimations INSTANCE = new HitAnimations ( );
    public Setting<AnimMode> animMode = register(new Setting("Mode", AnimMode.Normal));
    public HitAnimations() {
        super("HitAnimations", "1.8 hit animations", Module.Category.VISUAL, true, false, false);
        this.setInstance ( );
    }

    public static HitAnimations getInstance ( ) {
        if ( INSTANCE == null ) {
            INSTANCE = new HitAnimations();
        }
        return INSTANCE;
    }

    private
    void setInstance ( ) {
        INSTANCE = this;
    }

    @Override
    public String getDisplayInfo() {
        switch (this.animMode.getValue()) {
            case Normal:
                return "Normal";
            case Retarded:
                return "Retarded";
        }
        return "";
    }


    @Override
    public void onUpdate() {
        switch (this.animMode.getValue()) {
            case Normal:
                HitAnimations.mc.entityRenderer.itemRenderer.equippedProgressMainHand = 1.0f;
                HitAnimations.mc.entityRenderer.itemRenderer.equippedProgressOffHand = 1.0f;
                HitAnimations.mc.entityRenderer.itemRenderer.itemStackMainHand = HitAnimations.mc.player.getHeldItemMainhand();
                HitAnimations.mc.entityRenderer.itemRenderer.itemStackOffHand = HitAnimations.mc.player.getHeldItemOffhand();
            case Retarded:
                if (HitAnimations.mc.entityRenderer.itemRenderer.prevEquippedProgressMainHand >= 0.9) {
                    HitAnimations.mc.entityRenderer.itemRenderer.equippedProgressMainHand = 1.0f;
                    HitAnimations.mc.entityRenderer.itemRenderer.itemStackMainHand = HitAnimations.mc.player.getHeldItemMainhand();
                }
        }

    }

    public enum AnimMode {
        Retarded, Normal
    }
}

