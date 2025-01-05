package com.example.backend.s3.bucket

import aws.sdk.kotlin.services.s3.S3Client
import aws.sdk.kotlin.services.s3.model.*
import com.example.Constants

class S3BucketRequests : S3BucketRequestsInterface {

    override suspend fun createBucket(
        bucketName: String,
        bucketRegion: String,
    ) {
        val request = CreateBucketRequest {
            bucket = bucketName
        }

        S3Client { region = bucketRegion }.use { s3 ->
            s3.createBucket(request)
        }
    }

    override suspend fun deleteBucket(
        bucketName: String,
        bucketRegion: String,
    ) {
        val request = DeleteBucketRequest { bucket = bucketName }
        S3Client { region = bucketRegion }.use { s3 ->
            s3.deleteBucket(request)
        }
    }

    override suspend fun putPolicyOfBucket(
        bucketName: String,
        bucketPolicy: String,
        bucketRegion: String,
    ) {
        val request = PutBucketPolicyRequest {
            bucket = bucketName
            policy = bucketPolicy
        }
        S3Client { region = bucketRegion }.use { s3 ->
            s3.putBucketPolicy(request)
        }
    }

    override suspend fun headBucket(bucketName: String, bucketRegion: String) {
        val request = HeadBucketRequest {
            bucket = bucketName
        }
        S3Client { region = bucketRegion }.use { s3 ->
            s3.headBucket(request)
        }
    }

    override suspend fun getBucketPolicy(
        bucketName: String,
        bucketRegion: String,
    ): String? {
        val request = GetBucketPolicyRequest {
            bucket = bucketName
        }
        S3Client { region = bucketRegion }.use { s3 ->
            return s3.getBucketPolicy(request).policy
        }
    }
}

interface S3BucketRequestsInterface {

    private companion object: Constants() {
        private const val REGION = AWS_REGION
    }

    suspend fun createBucket(
        bucketName: String,
        bucketRegion: String = REGION,
    )

    suspend fun deleteBucket(
        bucketName: String,
        bucketRegion: String = REGION,
    )

    suspend fun putPolicyOfBucket(
        bucketName: String,
        bucketPolicy: String,
        bucketRegion: String = REGION,
    )

    suspend fun headBucket(
        bucketName: String,
        bucketRegion: String = REGION,
    )

    suspend fun getBucketPolicy(
        bucketName: String,
        bucketRegion: String,
    ): String?

}