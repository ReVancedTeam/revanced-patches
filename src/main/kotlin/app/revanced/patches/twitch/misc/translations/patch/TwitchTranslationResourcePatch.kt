package app.revanced.patches.twitch.misc.translations.patch

import app.revanced.patcher.annotation.Description
import app.revanced.patcher.annotation.Name
import app.revanced.patcher.data.ResourceContext
import app.revanced.patcher.patch.PatchResult
import app.revanced.patcher.patch.PatchResultSuccess
import app.revanced.patcher.patch.ResourcePatch
import app.revanced.patcher.patch.annotations.DependsOn
import app.revanced.patcher.patch.annotations.Patch
import app.revanced.patches.shared.settings.preference.impl.*
import app.revanced.patches.twitch.misc.settings.resource.patch.TwitchSettingsResourcePatch
import app.revanced.patches.twitch.misc.translations.annotation.TwitchTranslationCompatibility
import app.revanced.util.resources.ResourceUtils

//@Patch // TODO: release this after translations are usable
@Name("Translations")
@Description("Adds translations to ReVanced.")
@TwitchTranslationCompatibility
@DependsOn([TwitchSettingsResourcePatch::class])
class TwitchTranslationResourcePatch : ResourcePatch {

    override fun execute(context: ResourceContext): PatchResult {
        // Look in the jar file and find the paths of the translation string files.
        ResourceUtils.copyLocalizedStringFiles(context, TRANSLATION_RESOURCE_DIRECTORY)

        return PatchResultSuccess()
    }

    private companion object {
        const val TRANSLATION_RESOURCE_DIRECTORY = "twitch/translation"
    }

}