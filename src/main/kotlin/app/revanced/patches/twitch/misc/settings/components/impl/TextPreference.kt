package app.revanced.patches.twitch.misc.settings.components.impl

import app.revanced.patches.twitch.misc.settings.components.BasePreference

/**
 * Text preference.
 *
 * @param key The key of the text preference.
 * @param title The title of the text preference.
 * @param inputType The input type of the text preference.
 * @param default The default value of the text preference.
 * @param summary The summary of the text preference.
 */
internal class TextPreference(
    key: String,
    title: StringResource,
    var inputType: InputType = InputType.STRING,
    var default: String? = null,
    var summary: StringResource? = null
) : BasePreference(key, title) {
    override val tag: String = "EditTextPreference"
}