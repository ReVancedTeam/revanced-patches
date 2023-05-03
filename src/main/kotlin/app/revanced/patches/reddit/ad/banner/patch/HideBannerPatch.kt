package app.revanced.patches.reddit.ad.banner.patch

import app.revanced.patcher.ResourceContext
import app.revanced.patcher.annotation.Description
import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.patch.PatchException
import app.revanced.patcher.patch.ResourcePatch
import app.revanced.patcher.patch.annotations.Patch
import app.revanced.patches.reddit.ad.banner.annotations.HideBannerCompatibility
import app.revanced.util.resources.ResourceUtils.base

@Patch
@Name("hide-subreddit-banner")
@Description("Hides banner ads from comments on subreddits.")
@HideBannerCompatibility
@Version("0.0.1")
class HideBannerPatch : ResourcePatch {
    override fun execute(context: ResourceContext) {

        context.base.editXmlFile(RESOURCE_FILE_PATH).use {
            it.file.getElementsByTagName("merge").item(0).childNodes.apply {
                val attributes = arrayOf("height", "width")

                for (i in 1 until length) {
                    val view = item(i)
                    if (
                        view.hasAttributes() &&
                        view.attributes.getNamedItem("android:id").nodeValue.endsWith("ad_view_stub")
                    ) {
                        attributes.forEach { attribute ->
                            view.attributes.getNamedItem("android:layout_$attribute").nodeValue = "0.0dip"
                        }

                        break
                    }
                }
            }
        }

        return PatchResult.Success
    }

    private companion object {
        const val RESOURCE_FILE_PATH = "res/layout/merge_listheader_link_detail.xml"
    }
}

