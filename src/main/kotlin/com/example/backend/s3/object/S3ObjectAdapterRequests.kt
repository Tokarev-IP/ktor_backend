package com.example.backend.s3.`object`

import aws.sdk.kotlin.services.s3.model.DeleteObjectRequest
import aws.sdk.kotlin.services.s3.model.GetObjectRequest
import aws.sdk.kotlin.services.s3.model.HeadObjectRequest
import aws.sdk.kotlin.services.s3.model.PutObjectRequest
import aws.smithy.kotlin.runtime.content.ByteStream
import aws.smithy.kotlin.runtime.content.toByteArray
import com.example.Constants

class S3ObjectAdapterRequests(
    private val s3ObjectRequestsInterface: S3ObjectRequestsInterface,
) : S3ObjectAdapterRequestsInterface {

    override suspend fun putObject(
        objectName: String,
        objectAsByteStream: ByteStream,
        objectContentType: String,
        fileObjectUrlBase: String,
        bucketName: String
    ) {
        val request = PutObjectRequest {
            bucket = bucketName
            key = objectName
            contentType = objectContentType
            body = objectAsByteStream
        }

        s3ObjectRequestsInterface.putObject(request = request)
    }

    override suspend fun getObject(
        objectName: String,
        fileObjectUrlBase: String,
        bucketName: String
    ): ByteArray? {
        val request = GetObjectRequest {
            key = objectName
            bucket = bucketName
        }

        val response = s3ObjectRequestsInterface.getObject(request = request)
        val byteStream: ByteStream? = response.body
        return byteStream?.toByteArray()
    }

    override suspend fun deleteObject(
        objectName: String,
        fileObjectUrlBase: String,
        bucketName: String
    ) {
        val request = DeleteObjectRequest {
            bucket = bucketName
            key = objectName
        }

        s3ObjectRequestsInterface.deleteObject(request = request)
    }

    override suspend fun headObject(
        objectName: String,
        fileObjectUrlBase: String,
        bucketName: String
    ) {
        val request = HeadObjectRequest {
            bucket = bucketName
            key = objectName
        }

        s3ObjectRequestsInterface.headObject(request = request)
    }
}

interface S3ObjectAdapterRequestsInterface {

    private companion object: Constants() {
        private const val IMAGE_BUCKET_NAME = IMAGE_S3_BUCKET_NAME
        private const val IMAGE_URL_BASE = S3_IMAGE_URL_BASE
    }

    suspend fun putObject(
        objectName: String,
        objectAsByteStream: ByteStream,
        objectContentType: String,
        fileObjectUrlBase: String = IMAGE_URL_BASE,
        bucketName: String = IMAGE_BUCKET_NAME,
    )

    suspend fun getObject(
        objectName: String,
        fileObjectUrlBase: String = IMAGE_URL_BASE,
        bucketName: String = IMAGE_BUCKET_NAME,
    ): ByteArray?

    suspend fun deleteObject(
        objectName: String,
        fileObjectUrlBase: String = IMAGE_URL_BASE,
        bucketName: String = IMAGE_BUCKET_NAME,
    )

    suspend fun headObject(
        objectName: String,
        fileObjectUrlBase: String = IMAGE_URL_BASE,
        bucketName: String = IMAGE_BUCKET_NAME,
    )
}