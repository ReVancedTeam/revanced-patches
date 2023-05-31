package app.revanced.patches.youtube.layout.hide.personalinformation.resource.patch

import app.revanced.patcher.ResourceContext
import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.patch.ResourcePatch
import app.revanced.patcher.patch.annotations.DependsOn
import app.revanced.patches.shared.settings.preference.impl.StringResource
import app.revanced.patches.shared.settings.preference.impl.SwitchPreference
import app.revanced.patches.youtube.layout.hide.personalinformation.annotations.HideEmailAddressCompatibility
import app.revanced.patches.youtube.misc.settings.bytecode.patch.SettingsPatch
import app.revanced.util.resources.ResourceUtils.resourceIdOf

@Name("hide-email-address-resource-patch")
@HideEmailAddressCompatibility
@DependsOn([SettingsPatch::class])
@Version("0.0.1")
class HideEmailAddressResourcePatch : ResourcePatch {
    companion object {
        internal var accountSwitcherAccessibilityLabelId: Long = -1
    }

    override suspend fun execute(context: ResourceContext) {
        SettingsPatch.PreferenceScreen.LAYOUT.addPreferences(
            SwitchPreference(
                "revanced_hide_email_address",
                StringResource("revanced_hide_email_address_title", "Hide email in account switcher"),
                StringResource("revanced_hide_email_address_summary_on", "Email address is hidden"),
                StringResource("revanced_hide_email_address_summary_off", "Email address is shown")
            )
        )

        accountSwitcherAccessibilityLabelId = context.resourceIdOf("string", "account_switcher_accessibility_label")

    }
}