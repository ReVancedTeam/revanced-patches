package app.revanced.patches.youtube.layout.hide.general.patch

import app.revanced.extensions.toErrorResult
import app.revanced.patcher.annotation.Description
import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.data.BytecodeContext
import app.revanced.patcher.extensions.InstructionExtensions.addInstruction
import app.revanced.patcher.extensions.InstructionExtensions.addInstructionsWithLabels
import app.revanced.patcher.extensions.InstructionExtensions.getInstruction
import app.revanced.patcher.patch.BytecodePatch
import app.revanced.patcher.patch.PatchResult
import app.revanced.patcher.patch.PatchResultSuccess
import app.revanced.patcher.patch.annotations.DependsOn
import app.revanced.patcher.patch.annotations.Patch
import app.revanced.patcher.util.smali.ExternalLabel
import app.revanced.patches.shared.settings.preference.impl.SwitchPreference
import app.revanced.patches.shared.settings.preference.impl.TextPreference
import app.revanced.patches.youtube.layout.hide.general.annotations.HideLayoutComponentsCompatibility
import app.revanced.patches.youtube.layout.hide.general.fingerprints.ConvertElementToFlatBufferFingerprint
import app.revanced.patches.youtube.misc.litho.filter.patch.LithoFilterPatch
import app.revanced.patches.youtube.misc.settings.bytecode.patch.YouTubeSettingsPatch
import app.revanced.patches.youtube.misc.settings.bytecode.patch.YouTubeSettingsPatch.PreferenceScreen

@Patch
@Name("Hide layout components")
@Description("Hides general layout components.")
@DependsOn([LithoFilterPatch::class, YouTubeSettingsPatch::class])
@HideLayoutComponentsCompatibility
@Version("0.0.1")
class HideLayoutComponentsPatch : BytecodePatch(
    listOf(ConvertElementToFlatBufferFingerprint)
) {
    override fun execute(context: BytecodeContext): PatchResult {
        PreferenceScreen.LAYOUT.addPreferences(
            SwitchPreference(
                "revanced_hide_gray_separator",
                "revanced_hide_gray_separator_title",
                "revanced_hide_gray_separator_summary_on",
                "revanced_hide_gray_separator_summary_off",
            ),
            SwitchPreference(
                "revanced_hide_channel_guidelines",
                "revanced_hide_channel_guidelines_title",
                "revanced_hide_channel_guidelines_summary_on",
                "revanced_hide_channel_guidelines_summary_off",
            ),
            SwitchPreference(
                "revanced_hide_expandable_chip",
                "revanced_hide_expandable_chip_title",
                "revanced_hide_expandable_chip_summary_on",
                "revanced_hide_expandable_chip_summary_off",
            ),
            SwitchPreference(
                "revanced_hide_chapters",
                "revanced_hide_chapters_title",
                "revanced_hide_chapters_summary_on",
                "revanced_hide_chapters_summary_off",
            ),
            SwitchPreference(
                "revanced_hide_community_posts",
                "revanced_hide_community_posts_title",
                "revanced_hide_community_posts_summary_on",
                "revanced_hide_community_posts_summary_off",
            ),
            SwitchPreference(
                "revanced_hide_compact_banner",
                "revanced_hide_compact_banner_title",
                "revanced_hide_compact_banner_summary_on",
                "revanced_hide_compact_banner_summary_off",
            ),
            SwitchPreference(
                "revanced_hide_movies_section",
                "revanced_hide_movies_section_title",
                "revanced_hide_movies_section_summary_on",
                "revanced_hide_movies_section_summary_off",
            ),
            SwitchPreference(
                "revanced_hide_feed_survey",
                "revanced_hide_feed_survey_title",
                "revanced_hide_feed_survey_summary_on",
                "revanced_hide_feed_survey_summary_off",
            ),
            SwitchPreference(
                "revanced_hide_community_guidelines",
                "revanced_hide_community_guidelines_title",
                "revanced_hide_community_guidelines_summary_on",
                "revanced_hide_community_guidelines_summary_off",
            ),
            SwitchPreference(
                "revanced_hide_subscribers_community_guidelines",
                "revanced_hide_subscribers_community_guidelines_title",
                "revanced_hide_subscribers_community_guidelines_summary_on",
                "revanced_hide_subscribers_community_guidelines_summary_off",
            ),
            SwitchPreference(
                "revanced_hide_channel_member_shelf",
                "revanced_hide_channel_member_shelf_title",
                "revanced_hide_channel_member_shelf_summary_on",
                "revanced_hide_channel_member_shelf_summary_off",
            ),
            SwitchPreference(
                "revanced_hide_emergency_box",
                "revanced_hide_emergency_box_title",
                "revanced_hide_emergency_box_summary_on",
                "revanced_hide_emergency_box_summary_off",
            ),
            SwitchPreference(
                "revanced_hide_info_panels",
                "revanced_hide_info_panels_title",
                "revanced_hide_info_panels_summary_on",
                "revanced_hide_info_panels_summary_off",
            ),
            SwitchPreference(
                "revanced_hide_medical_panels",
                "revanced_hide_medical_panels_title",
                "revanced_hide_medical_panels_summary_on",
                "revanced_hide_medical_panels_summary_off",
            ),
            SwitchPreference(
                "revanced_hide_channel_bar",
                "revanced_hide_channel_bar_title",
                "revanced_hide_channel_bar_summary_on",
                "revanced_hide_channel_bar_summary_off",
            ),
            SwitchPreference(
                "revanced_hide_quick_actions",
                "revanced_hide_quick_actions_title",
                "revanced_hide_quick_actions_summary_on",
                "revanced_hide_quick_actions_summary_off",
            ),
            SwitchPreference(
                "revanced_hide_related_videos",
                "revanced_hide_related_videos_title",
                "revanced_hide_related_videos_summary_on",
                "revanced_hide_related_videos_summary_off",
            ),
            SwitchPreference(
                "revanced_hide_image_shelf",
                "revanced_hide_image_shelf",
                "revanced_hide_image_shelf_summary_on",
                "revanced_hide_image_shelf_summary_off",
            ),
            SwitchPreference(
                "revanced_hide_audio_track_button",
                "revanced_hide_audio_track_button_title",
                "revanced_hide_audio_track_button_on",
                "revanced_hide_audio_track_button_off",
            ),
            SwitchPreference(
                "revanced_hide_latest_posts_ads",
                "revanced_hide_latest_posts_ads_title",
                "revanced_hide_latest_posts_ads_summary_on",
                "revanced_hide_latest_posts_ads_summary_off",
            ),
            SwitchPreference(
                "revanced_hide_mix_playlists",
                "revanced_hide_mix_playlists_title",
                "revanced_hide_mix_playlists_summary_on",
                "revanced_hide_mix_playlists_summary_off",
            ),
            SwitchPreference(
                "revanced_hide_artist_cards",
                "revanced_hide_artist_cards_title",
                "revanced_hide_artist_cards_on",
                "revanced_hide_artist_cards_off",
            ),
            SwitchPreference(
                "revanced_hide_chips_shelf",
                "revanced_hide_chips_shelf_title",
                "revanced_hide_chips_shelf_on",
                "revanced_hide_chips_shelf_off",
            ),
            app.revanced.patches.shared.settings.preference.impl.PreferenceScreen(
                "revanced_custom_filter_preference_screen",
                "revanced_custom_filter_preference_screen_title",
                listOf(
                    SwitchPreference(
                        "revanced_custom_filter",
                        "revanced_custom_filter_title",
                        "revanced_custom_filter_summary_on",
                        "revanced_custom_filter_summary_off",
                    ),
                    // TODO: This should be a dynamic ListPreference, which does not exist yet
                    TextPreference(
                        "revanced_custom_filter_strings",
                        "revanced_custom_filter_strings_title",
                        "revanced_custom_filter_strings_summary",
                    )
                )
            )
        )

        LithoFilterPatch.addFilter(FILTER_CLASS_DESCRIPTOR)

        // region Mix playlists

        ConvertElementToFlatBufferFingerprint.result?.let {
            val returnEmptyComponentIndex = it.scanResult.stringsScanResult!!.matches.first().index + 2

            it.mutableMethod.apply {
                // The last virtual register (not parameter). Used to store the byte array
                // that may contain information about a mix playlist.
                val freeRegister = (implementation!!.registerCount - 1) - parameterTypes.size - 1

                // Check if the byte array contains anything about a mix playlist.
                addInstructionsWithLabels(
                    it.scanResult.patternScanResult!!.startIndex,
                    """
                        invoke-static {v$freeRegister}, $FILTER_CLASS_DESCRIPTOR->filterMixPlaylists([B)Z
                        move-result v$freeRegister
                        if-nez v$freeRegister, :return_empty_component
                    """,
                    ExternalLabel("return_empty_component", getInstruction(returnEmptyComponentIndex))
                )

                // Move the byte array to a free register.
                addInstruction(0, "move-object/from16 v$freeRegister, p3")
            }

        } ?: return ConvertElementToFlatBufferFingerprint.toErrorResult()

        // endregion

        return PatchResultSuccess()
    }

    internal companion object {
        private const val FILTER_CLASS_DESCRIPTOR =
            "Lapp/revanced/integrations/patches/components/LayoutComponentsFilter;"
    }
}
