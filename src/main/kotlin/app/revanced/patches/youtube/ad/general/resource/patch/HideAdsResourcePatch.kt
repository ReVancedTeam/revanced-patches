package app.revanced.patches.youtube.ad.general.resource.patch

import app.revanced.patcher.annotation.Version
import app.revanced.patcher.data.ResourceContext
import app.revanced.patcher.patch.PatchResult
import app.revanced.patcher.patch.PatchResultSuccess
import app.revanced.patcher.patch.ResourcePatch
import app.revanced.patcher.patch.annotations.DependsOn
import app.revanced.patches.shared.mapping.misc.patch.ResourceMappingPatch
import app.revanced.patches.shared.settings.preference.impl.*
import app.revanced.patches.youtube.ad.general.annotation.HideAdsCompatibility
import app.revanced.patches.youtube.misc.litho.filter.patch.LithoFilterPatch
import app.revanced.patches.youtube.misc.settings.bytecode.patch.YouTubeSettingsPatch
import app.revanced.patches.youtube.misc.settings.bytecode.patch.YouTubeSettingsPatch.PreferenceScreen

@DependsOn(
    dependencies = [
        LithoFilterPatch::class,
        YouTubeSettingsPatch::class,
        ResourceMappingPatch::class
    ]
)
@HideAdsCompatibility
@Version("0.0.1")
class HideAdsResourcePatch : ResourcePatch {

    override fun execute(context: ResourceContext): PatchResult {
        PreferenceScreen.LAYOUT.addPreferences(
            SwitchPreference(
                "revanced_hide_gray_separator",
                "revanced_hide_gray_separator_title",
                "revanced_hide_gray_separator_summary_on",
                "revanced_hide_gray_separator_summary_off"
            ),
            SwitchPreference(
                "revanced_hide_channel_guidelines",
                "revanced_hide_channel_guidelines_title",
                "revanced_hide_channel_guidelines_summary_on",
                "revanced_hide_channel_guidelines_summary_off"
            ),
            SwitchPreference(
                "revanced_hide_chapter_teaser",
                "revanced_hide_chapter_teaser_title",
                "revanced_hide_chapter_teaser_summary_on",
                "revanced_hide_chapter_teaser_summary_off"
            ),
            SwitchPreference(
                "revanced_hide_merchandise_banners",
                "revanced_hide_merchandise_banners_title",
                "revanced_hide_merchandise_banners_summary_on",
                "revanced_hide_merchandise_banners_summary_off"
            ),
            SwitchPreference(
                "revanced_hide_community_posts",
                "revanced_hide_community_posts_title",
                "revanced_hide_community_posts_summary_on",
                "revanced_hide_community_posts_summary_off"
            ),
            SwitchPreference(
                "revanced_hide_compact_banner",
                "revanced_hide_compact_banner_title",
                "revanced_hide_compact_banner_summary_on",
                "revanced_hide_compact_banner_summary_off"
            ),
            SwitchPreference(
                "revanced_hide_products_banner",
                "revanced_hide_products_banner_title",
                "revanced_hide_products_banner_summary_on",
                "revanced_hide_products_banner_summary_off"
            ),
            SwitchPreference(
                "revanced_hide_web_search_results",
                "revanced_hide_web_search_results_title",
                "revanced_hide_web_search_results_summary_on",
                "revanced_hide_web_search_results_summary_off"
            ),
            SwitchPreference(
                "revanced_hide_movies_section",
                "revanced_hide_movies_section_title",
                "revanced_hide_movies_section_summary_on",
                "revanced_hide_movies_section_summary_off"
            ),
            SwitchPreference(
                "revanced_hide_feed_survey",
                "revanced_hide_feed_survey_title",
                "revanced_hide_feed_survey_summary_on",
                "revanced_hide_feed_survey_summary_off"
            ),
            SwitchPreference(
                "revanced_hide_community_guidelines",
                "revanced_hide_community_guidelines_title",
                "revanced_hide_community_guidelines_summary_on",
                "revanced_hide_community_guidelines_summary_off"
            ),
            SwitchPreference(
                "revanced_hide_subscribers_community_guidelines",
                "revanced_hide_subscribers_community_guidelines_title",
                "revanced_hide_subscribers_community_guidelines_summary_on",
                "revanced_hide_subscribers_community_guidelines_summary_off"
            ),
            SwitchPreference(
                "revanced_hide_channel_member_shelf",
                "revanced_hide_channel_member_shelf_title",
                "revanced_hide_channel_member_shelf_summary_on",
                "revanced_hide_channel_member_shelf_summary_off"
            ),
            SwitchPreference(
                "revanced_hide_emergency_box",
                "revanced_hide_emergency_box_title",
                "revanced_hide_emergency_box_summary_on",
                "revanced_hide_emergency_box_summary_off"
            ),
            SwitchPreference(
                "revanced_hide_info_panels",
                "revanced_hide_info_panels_title",
                "revanced_hide_info_panels_summary_on",
                "revanced_hide_info_panels_summary_off"
            ),
            SwitchPreference(
                "revanced_hide_medical_panels",
                "revanced_hide_medical_panels_title",
                "revanced_hide_medical_panels_summary_on",
                "revanced_hide_medical_panels_summary_off"
            ),
            SwitchPreference(
                "revanced_hide_channel_bar",
                "revanced_hide_channel_bar_title",
                "revanced_hide_channel_bar_summary_on",
                "revanced_hide_channel_bar_summary_off"
            ),
            SwitchPreference(
                "revanced_hide_quick_actions",
                "revanced_hide_quick_actions_title",
                "revanced_hide_quick_actions_summary_on",
                "revanced_hide_quick_actions_summary_off"
            ),
            SwitchPreference(
                "revanced_hide_related_videos",
                "revanced_hide_related_videos_title",
                "revanced_hide_related_videos_summary_on",
                "revanced_hide_related_videos_summary_off"
            ),
            SwitchPreference(
                "revanced_hide_image_shelf",
                "revanced_hide_image_shelf",
                "revanced_hide_image_shelf_summary_on",
                "revanced_hide_image_shelf_summary_off"
            ),
            SwitchPreference(
                "revanced_hide_audio_track_button",
                "revanced_hide_audio_track_button_title",
                "revanced_hide_audio_track_button_on",
                "revanced_hide_audio_track_button_off"
            ),
            SwitchPreference(
                "revanced_hide_latest_posts_ads",
                "revanced_hide_latest_posts_ads_title",
                "revanced_hide_latest_posts_ads_summary_on",
                "revanced_hide_latest_posts_ads_summary_off"
            ),
            SwitchPreference(
                "revanced_hide_mix_playlists",
                "revanced_hide_mix_playlists_title",
                "revanced_hide_mix_playlists_summary_on",
                "revanced_hide_mix_playlists_summary_off"
            ),
        )

        PreferenceScreen.ADS.addPreferences(
            SwitchPreference(
                "revanced_hide_general_ads",
                "revanced_hide_general_ads_title",
                "revanced_hide_general_ads_summary_on",
                "revanced_hide_general_ads_summary_off"
            ),
            SwitchPreference(
                "revanced_hide_buttoned_ads",
                "revanced_hide_buttoned_ads_title",
                "revanced_hide_buttoned_ads_summary_on",
                "revanced_hide_buttoned_ads_summary_off"
            ),
            SwitchPreference(
                "revanced_hide_paid_content_ads",
                "revanced_hide_paid_content_ads_title",
                "revanced_hide_paid_content_ads_summary_on",
                "revanced_hide_paid_content_ads_summary_off"
            ),
            SwitchPreference(
                "revanced_hide_self_sponsor_ads",
                "revanced_hide_self_sponsor_ads_title",
                "revanced_hide_self_sponsor_ads_summary_on",
                "revanced_hide_self_sponsor_ads_summary_off"
            ),
            PreferenceScreen(
                "revanced_custom_filter_preference_screen",
                "revanced_custom_filter_preference_screen_title",
                listOf(
                    SwitchPreference(
                        "revanced_custom_filter",
                        "revanced_custom_filter_title",
                        "revanced_custom_filter_summary_on",
                        "revanced_custom_filter_summary_off"
                    ),
                    // TODO: This should be a dynamic ListPreference, which does not exist yet
                    TextPreference(
                        "revanced_custom_filter_strings",
                        "revanced_custom_filter_strings_title",
                        "revanced_custom_filter_strings_summary"
                    )
                )
            )
        )

        adAttributionId = ResourceMappingPatch.resourceMappings.single { it.name == "ad_attribution" }.id

        return PatchResultSuccess()
    }

    internal companion object {
        var adAttributionId: Long = -1
    }
}