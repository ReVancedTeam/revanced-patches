package app.revanced.patches.youtube.layout.buttons.player.background.patch

import app.revanced.patcher.ResourceContext
import app.revanced.patcher.annotation.Description
import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.patch.PatchException
import app.revanced.patcher.patch.ResourcePatch
import app.revanced.patcher.patch.annotations.Patch
import app.revanced.patcher.util.dom.DomUtil.doRecursively
import app.revanced.patches.youtube.layout.buttons.player.background.annotations.PlayerButtonBackgroundCompatibility
import app.revanced.util.resources.ResourceUtils.base
import org.w3c.dom.Element

@Patch
@Name("remove-player-button-background")
@Description("Removes the background from the video player buttons.")
@PlayerButtonBackgroundCompatibility
@Version("0.0.1")
class PlayerButtonBackgroundPatch : ResourcePatch {
    private companion object {
        const val RESOURCE_FILE_PATH = "res/drawable/player_button_circle_background.xml"
    }
    
    override fun execute(context: ResourceContext) {
        context.base.editXmlFile(RESOURCE_FILE_PATH).use { editor ->
            editor.file.doRecursively node@{ node ->
                if (node !is Element) return@node

                node.getAttributeNode("android:color")?.let { attribute ->
                    attribute.textContent = "@android:color/transparent"
                }
            }
        }

        return PatchResult.Success
    }
}
