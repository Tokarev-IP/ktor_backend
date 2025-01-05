package com.example.backend.s3.`object`.image

import aws.smithy.kotlin.runtime.content.ByteStream
import com.example.backend.s3.`object`.S3ObjectAdapterRequestsInterface

class S3ImageObjectUseCases(
    private val s3ObjectAdapterRequestsInterface: S3ObjectAdapterRequestsInterface,
) : S3ImageObjectUseCasesInterface {

    override suspend fun putObjectIntoBucket(
        objectName: String,
        objectBytes: ByteArray,
        objectType: String,
    ) {
        s3ObjectAdapterRequestsInterface.putObject(
            objectName = objectName,
            objectAsByteStream = ByteStream.fromBytes(objectBytes),
            objectContentType = objectType,
        )
    }

    override suspend fun deleteObjectFromBucket(
        objectName: String,
    ) {
        s3ObjectAdapterRequestsInterface.deleteObject(
            objectName = objectName,
        )
    }

    override suspend fun getObjectFromBucket(
        objectName: String,
    ): ByteArray? {
        val byteArray = s3ObjectAdapterRequestsInterface.getObject(
            objectName = objectName
        )
        return byteArray
    }

    override suspend fun isObjectExistedAtBucket(
        objectName: String,
    ): Boolean {
        try {
            s3ObjectAdapterRequestsInterface.headObject(
                objectName = objectName,
            )
            return true
        } catch (e: Exception) {
            return false
        }
    }

}

interface S3ImageObjectUseCasesInterface {

    suspend fun putObjectIntoBucket(
        objectName: String,
        objectBytes: ByteArray,
        objectType: String,
    )

    suspend fun deleteObjectFromBucket(
        objectName: String,
    )

    suspend fun getObjectFromBucket(
        objectName: String,
    ): ByteArray?

    suspend fun isObjectExistedAtBucket(
        objectName: String,
    ): Boolean
}