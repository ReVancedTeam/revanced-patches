
package app.revanced.util.resources

import app.revanced.patcher.data.DomFileEditor
import app.revanced.patcher.data.ResourceContext
import app.revanced.patcher.patch.PatchResultError
import app.revanced.patches.shared.settings.resource.patch.AbstractSettingsResourcePatch
import org.w3c.dom.Node
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import java.util.jar.JarFile
import java.util.regex.Pattern

internal object ResourceUtils {

    /**
     * Merge strings in the default Strings.xml file
     * @param host The hosting xml resource. Needs to be a valid strings.xml resource.
     */
    internal fun ResourceContext.mergeStrings(host: String) {
        this.iterateXmlNodeChildren(host, "resources") {
            // TODO: figure out why this is needed
            if (!it.hasAttributes()) return@iterateXmlNodeChildren

            val attributes = it.attributes
            val key = attributes.getNamedItem("name")!!.nodeValue!!
            val value = it.textContent!!

            val formatted = attributes.getNamedItem("formatted") == null

            AbstractSettingsResourcePatch.addString(key, value, formatted)
        }
    }

    /**
     * Copy resources from the current class loader to the resource directory.
     * @param sourceResourceDirectory The source resource directory name.
     * @param resources The resources to copy.
     */
    internal fun ResourceContext.copyResources(sourceResourceDirectory: String, vararg resources: ResourceGroup) {
        val classLoader = ResourceUtils.javaClass.classLoader
        val targetResourceDirectory = this["res"]

        for (resourceGroup in resources) {
            resourceGroup.resources.forEach { resource ->
                // Create the target directory if it does not exist
                this[targetResourceDirectory.resolve(resourceGroup.resourceDirectoryName).absolutePath].mkdir()
                val resourceFile = "${resourceGroup.resourceDirectoryName}/$resource"
                Files.copy(
                    classLoader.getResourceAsStream("$sourceResourceDirectory/$resourceFile")!!,
                    targetResourceDirectory.resolve(resourceFile).toPath(), StandardCopyOption.REPLACE_EXISTING
                )
            }
        }
    }

    /**
     * Copy localized strings from a given directory.
     */
    internal fun copyLocalizedStringFiles(context: ResourceContext, directory: String) {
        val pattern = Pattern.compile("$directory/([-_a-zA-Z0-9]+)/strings\\.xml")

        var jf: JarFile? = null
        try {
            jf = JarFile(this.javaClass.protectionDomain.codeSource.location.toURI().path)
            val entries = jf.entries()
            var foundElements = false
            while (entries.hasMoreElements()) {
                val match = pattern.matcher(entries.nextElement().name)
                if (match.find()) {
                    val languageDirectory = match.group(1)
                    context.copyResources(
                        directory,
                        ResourceGroup(languageDirectory, "strings.xml")
                    )
                    foundElements = true
                }
            }
            if (!foundElements) throw PatchResultError("could not find translated string files")
        } finally {
            jf?.close()
        }
    }


    /**
     * Resource names mapped to their corresponding resource data.
     * @param resourceDirectoryName The name of the directory of the resource.
     * @param resources A list of resource names.
     */
    internal class ResourceGroup(val resourceDirectoryName: String, vararg val resources: String)

    /**
     * Iterate through the children of a node by its tag.
     * @param resource The xml resource.
     * @param targetTag The target xml node.
     * @param callback The callback to call when iterating over the nodes.
     */
    internal fun ResourceContext.iterateXmlNodeChildren(
        resource: String,
        targetTag: String,
        callback: (node: Node) -> Unit
    ) =
        xmlEditor[ResourceUtils.javaClass.classLoader.getResourceAsStream(resource)!!].use {
            val stringsNode = it.file.getElementsByTagName(targetTag).item(0).childNodes
            for (i in 1 until stringsNode.length - 1) callback(stringsNode.item(i))
        }


    /**
     * Copies the specified node of the source [DomFileEditor] to the target [DomFileEditor].
     * @param source the source [DomFileEditor].
     * @param target the target [DomFileEditor]-
     * @return AutoCloseable that closes the target [DomFileEditor]s.
     */
    fun String.copyXmlNode(source: DomFileEditor, target: DomFileEditor): AutoCloseable {
        val hostNodes = source.file.getElementsByTagName(this).item(0).childNodes

        val destinationResourceFile = target.file
        val destinationNode = destinationResourceFile.getElementsByTagName(this).item(0)

        for (index in 0 until hostNodes.length) {
            val node = hostNodes.item(index).cloneNode(true)
            destinationResourceFile.adoptNode(node)
            destinationNode.appendChild(node)
        }

        return AutoCloseable {
            source.close()
            target.close()
        }
    }
}