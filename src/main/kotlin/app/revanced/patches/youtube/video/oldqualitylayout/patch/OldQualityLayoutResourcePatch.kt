package app.revanced.patches.youtube.video.oldqualitylayout.patch

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
class OldQualityLayoutResourcePatch : ResourcePatch {
    override fun execute(context: ResourceContext): PatchResult {
        YouTubeSettingsPatch.PreferenceScreen.VIDEO.addPreferences(
            SwitchPreference(
                "revanced_show_old_video_menu",
                "revanced_show_old_video_menu_title",
                "revanced_show_old_video_menu_summary_on",
                "revanced_show_old_video_menu_summary_off"
            )
        )

        videoQualityBottomSheetListFragmentTitle =
            ResourceMappingPatch.resourceMappings.find { it.name == "video_quality_bottom_sheet_list_fragment_title" }
                ?.id ?: return PatchResultError("Could not find resource")

        return PatchResultSuccess()
    }

    internal companion object {
        var videoQualityBottomSheetListFragmentTitle = -1L
    }
}