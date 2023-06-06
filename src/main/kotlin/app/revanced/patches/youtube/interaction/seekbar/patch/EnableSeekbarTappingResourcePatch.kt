package app.revanced.patches.youtube.interaction.seekbar.patch

import app.revanced.patcher.annotation.Version
import app.revanced.patcher.data.ResourceContext
import app.revanced.patcher.patch.PatchResult
import app.revanced.patcher.patch.PatchResultError
import app.revanced.patcher.patch.PatchResultSuccess
import app.revanced.patcher.patch.ResourcePatch
import app.revanced.patcher.patch.annotations.DependsOn
import app.revanced.patches.shared.mapping.misc.patch.ResourceMappingPatch
import app.revanced.patches.shared.settings.preference.impl.SwitchPreference
import app.revanced.patches.youtube.misc.settings.bytecode.patch.YouTubeSettingsPatch

@DependsOn([YouTubeSettingsPatch::class, ResourceMappingPatch::class])
@Version("0.0.1")
class EnableSeekbarTappingResourcePatch : ResourcePatch {
    override fun execute(context: ResourceContext): PatchResult {
        YouTubeSettingsPatch.PreferenceScreen.INTERACTIONS.addPreferences(
            SwitchPreference(
                "revanced_seekbar_tapping",
                "revanced_seekbar_tapping_title",
                "revanced_seekbar_tapping_summary_on",
                "revanced_seekbar_tapping_summary_off"
            )
        )

        accessibilityPlayerProgressTime = ResourceMappingPatch.resourceMappings.find {
            it.name == "accessibility_player_progress_time"
        }?.id ?: return PatchResultError("Failed to find required resource")

        return PatchResultSuccess()
    }

    internal companion object {
        var accessibilityPlayerProgressTime = -1L
    }
}