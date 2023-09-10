package app.revanced.patches.youtube.layout.hide.infocards.resource.patch

import app.revanced.patcher.data.ResourceContext
import app.revanced.patcher.patch.ResourcePatch
import app.revanced.patcher.patch.annotations.DependsOn
import app.revanced.patches.shared.mapping.misc.patch.ResourceMappingPatch
import app.revanced.patches.shared.settings.preference.impl.SwitchPreference
import app.revanced.patches.youtube.layout.hide.infocards.annotations.HideInfocardsCompatibility
import app.revanced.patches.youtube.misc.settings.bytecode.patch.SettingsPatch
import app.revanced.patches.youtube.misc.settings.resource.patch.SettingsResourcePatch

@HideInfocardsCompatibility
@DependsOn([SettingsPatch::class, ResourceMappingPatch::class])
class HideInfocardsResourcePatch : ResourcePatch {
    internal companion object {
        var drawerResourceId: Long = -1
    }

    override fun execute(context: ResourceContext) {
        SettingsPatch.PreferenceScreen.LAYOUT.addPreferences(
            SwitchPreference(
                "revanced_hide_info_cards",
                "revanced_hide_info_cards_title",
                "revanced_hide_info_cards_summary_on",
                "revanced_hide_info_cards_summary_off"
            )
        )
        SettingsResourcePatch.mergePatchStrings("HideInfocards")

        drawerResourceId = ResourceMappingPatch.resourceMappings.single {
            it.type == "id" && it.name == "info_cards_drawer_header"
        }.id
    }
}