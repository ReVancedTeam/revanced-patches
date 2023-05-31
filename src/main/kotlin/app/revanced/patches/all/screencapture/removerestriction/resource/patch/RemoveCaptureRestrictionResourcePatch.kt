package app.revanced.patches.all.screencapture.removerestriction.resource.patch

import app.revanced.patcher.ResourceContext
import app.revanced.patcher.annotation.Description
import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.patch.ResourcePatch
import app.revanced.util.resources.ResourceUtils.manifestEditor
import org.w3c.dom.Element

@Name("remove-screen-capture-restriction-resource-patch")
@Description("Sets allowAudioPlaybackCapture in manifest to true.")
@Version("0.0.1")
internal class RemoveCaptureRestrictionResourcePatch : ResourcePatch {
    override suspend fun execute(context: ResourceContext) {
        // create an xml editor instance
        context.manifestEditor().use { dom ->
            // get the application node
            val applicationNode = dom
                .file
                .getElementsByTagName("application")
                .item(0) as Element

            // set allowAudioPlaybackCapture attribute to true
            applicationNode.setAttribute("android:allowAudioPlaybackCapture", "true")
        }

    }
}