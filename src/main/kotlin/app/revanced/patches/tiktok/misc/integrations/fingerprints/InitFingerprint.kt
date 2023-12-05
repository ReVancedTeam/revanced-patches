package app.revanced.patches.tiktok.misc.integrations.fingerprints

import app.revanced.patches.shared.integrations.AbstractIntegrationsPatch.IntegrationsFingerprint

internal object InitFingerprint : IntegrationsFingerprint(
    customFingerprint = { methodDef, _ ->
        methodDef.definingClass.endsWith("/AwemeHostApplication;") &&
                methodDef.name == "onCreate"
    }
)