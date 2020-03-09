package me.tangni.cybertron

import java.io.InputStream

interface CybertronClassEditor {

    /**
     * returning null means you don't want to edit this class
     *
     * @param inputStream
     * @param fullQualifiedClassName
     * @return the edited class's byte array, or null
     */
    fun getEditedClassByteArray(inputStream: InputStream, fullQualifiedClassName: String): ByteArray?
}