package me.tangni.cybertron

import java.io.InputStream

interface CybertronClassEditor {

    /**
     * return the edited class's byte array, or null this class is not edited
     *
     * @param cybertron
     * @param inputStream
     * @param fullQualifiedClassName
     * @return the edited class's byte array, or null
     */
    fun getEditedClassByteArray(cybertron: Cybertron, inputStream: InputStream, fullQualifiedClassName: String): ByteArray?
}