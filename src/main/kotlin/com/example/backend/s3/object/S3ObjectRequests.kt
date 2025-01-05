package com.example.backend.s3.`object`

import aws.sdk.kotlin.services.s3.S3Client
import aws.sdk.kotlin.services.s3.model.*
import com.example.Constants

class S3ObjectRequests : S3ObjectRequestsInterface {

    override suspend fun putObject(
        request: PutObjectRequest,
        bucketRegion: String,
    ) {
        S3Client { region = bucketRegion }.use { s3 ->
            s3.putObject(request)
        }
    }

    override suspend fun getObject(
        request: GetObjectRequest,
        bucketRegion: String
    ): GetObjectResponse {
        S3Client { region = bucketRegion }.use { s3 ->
            return s3.getObject(request) { it }
        }
    }

    override suspend fun deleteObject(
        request: DeleteObjectRequest,
        bucketRegion: String,
    ) {
        S3Client { region = bucketRegion }.use { s3 ->
            s3.deleteObject(request)
        }
    }

    override suspend fun headObject(
        request: HeadObjectRequest,
        bucketRegion: String,
    ) {
        S3Client { region = bucketRegion }.use { s3 ->
            s3.headObject(request)
        }
    }
}

interface S3ObjectRequestsInterface {

    private companion object: Constants() {
        private const val REGION = AWS_REGION
    }

    suspend fun putObject(
        request: PutObjectRequest,
        bucketRegion: String = REGION,
    )

    suspend fun getObject(
        request: GetObjectRequest,
        bucketRegion: String = REGION,
    ): GetObjectResponse

    suspend fun deleteObject(
        request: DeleteObjectRequest,
        bucketRegion: String = REGION,
    )

    suspend fun headObject(
        request: HeadObjectRequest,
        bucketRegion: String = REGION,
    )
}