package app.revanced.patches.reddit.misc.disablescreenshotpopup.annotations

import app.revanced.patcher.annotation.Compatibility
import app.revanced.patcher.annotation.Package

@Compatibility(
    [Package(
        "com.reddit.frontpage"
    )]
)
@Target(AnnotationTarget.CLASS)
internal annotation class DisableScreenshotPopupCompatibility