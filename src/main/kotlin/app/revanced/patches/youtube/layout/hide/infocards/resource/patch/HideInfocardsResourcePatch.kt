package app.revanced.patches.youtube.layout.hide.infocards.resource.patch

import app.revanced.patcher.ResourceContext
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.patch.ResourcePatch
import app.revanced.patcher.patch.annotations.DependsOn
import app.revanced.patches.shared.settings.preference.impl.StringResource
import app.revanced.patches.shared.settings.preference.impl.SwitchPreference
import app.revanced.patches.youtube.layout.hide.infocards.annotations.HideInfocardsCompatibility
import app.revanced.patches.youtube.misc.settings.bytecode.patch.SettingsPatch
import app.revanced.util.resources.ResourceUtils.resourceIdOf

@HideInfocardsCompatibility
@DependsOn([SettingsPatch::class])
@Version("0.0.1")
class HideInfocardsResourcePatch : ResourcePatch {
    internal companion object {
        var drawerResourceId: Long = -1
    }

    override suspend fun execute(context: ResourceContext) {
        SettingsPatch.PreferenceScreen.LAYOUT.addPreferences(
            SwitchPreference(
                "revanced_hide_info_cards",
                StringResource("revanced_hide_info_cards_title", "Hide info cards"),
                StringResource("revanced_hide_info_cards_summary_on", "Info cards are hidden"),
                StringResource("revanced_hide_info_cards_summary_off", "Info cards are shown")
            )
        )

        drawerResourceId = context.resourceIdOf("id", "info_cards_drawer_header")

    }
}