package app.revanced.patches.youtube.layout.autocaptions.patch

import app.revanced.patcher.annotation.Description
import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.data.impl.BytecodeData
import app.revanced.patcher.extensions.addInstructions
import app.revanced.patcher.patch.PatchResult
import app.revanced.patcher.patch.PatchResultSuccess
import app.revanced.patcher.patch.annotations.DependsOn
import app.revanced.patcher.patch.annotations.Patch
import app.revanced.patcher.patch.impl.BytecodePatch
import app.revanced.patches.youtube.layout.autocaptions.annotations.AutoCaptionsCompatibility
import app.revanced.patches.youtube.layout.autocaptions.fingerprints.StartVideoInformerFingerprint
import app.revanced.patches.youtube.layout.autocaptions.fingerprints.SubtitleButtonControllerFingerprint
import app.revanced.patches.youtube.layout.autocaptions.fingerprints.SubtitleTrackFingerprint
import app.revanced.patches.youtube.misc.integrations.patch.IntegrationsPatch
import app.revanced.patches.youtube.misc.settings.bytecode.patch.SettingsPatch
import app.revanced.patches.youtube.misc.settings.framework.components.impl.StringResource
import app.revanced.patches.youtube.misc.settings.framework.components.impl.SwitchPreference

@Patch
@DependsOn([IntegrationsPatch::class, SettingsPatch::class])
@Name("forced-autocaptions-disabler")
@Description("Disable forced captions inside video player.")
@AutoCaptionsCompatibility
@Version("0.0.1")
class AutoCaptionsPatch : BytecodePatch(
    listOf(
        StartVideoInformerFingerprint, SubtitleButtonControllerFingerprint, SubtitleTrackFingerprint
    )
) {
    override fun execute(data: BytecodeData): PatchResult {
        SettingsPatch.PreferenceScreen.LAYOUT.addPreferences(
            SwitchPreference(
                "revanced_autocaptions_enabled",
                StringResource("revanced_autocaptions_enabled_title", "Disable forced captions"),
                false,
                StringResource("revanced_autocaptions_summary_on", "Forced captions is enabled"),
                StringResource("revanced_autocaptions_summary_off", "Forced captions is disabled")
            )
        )

        val startVideoInformerMethod = StartVideoInformerFingerprint.result!!.mutableMethod

        startVideoInformerMethod.addInstructions(
            0, """
            const/4 v0, 0x0
            sput-boolean v0, Lapp/revanced/integrations/patches/ForceDisableCaptionsPatch;->captionsButtonDisabled:Z
        """
        )

        val subtitleButtonControllerMethod = SubtitleButtonControllerFingerprint.result!!.mutableMethod

        subtitleButtonControllerMethod.addInstructions(
            0, """
            const/4 v0, 0x1
            sput-boolean v0, Lapp/revanced/integrations/patches/ForceDisableCaptionsPatch;->captionsButtonDisabled:Z
        """
        )

        val subtitleTrackMethod = SubtitleTrackFingerprint.result!!.mutableMethod

        subtitleTrackMethod.addInstructions(
            0, """
            invoke-static {}, Lapp/revanced/integrations/patches/ForceDisableCaptionsPatch;->autoCaptionsEnabled()Z
            move-result v0
            if-eqz v0, :forced_captions
            sget-boolean v0, Lapp/revanced/integrations/patches/ForceDisableCaptionsPatch;->captionsButtonDisabled:Z
            if-nez v0, :forced_captions
            const/4 v0, 0x1
            return v0
            :forced_captions
            nop
        """
        )

        return PatchResultSuccess()
    }
}