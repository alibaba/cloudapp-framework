/*
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.alibaba.cloudapp.api.filestore;

import com.alibaba.cloudapp.exeption.CloudAppException;

import java.io.InputStream;
import java.util.Collection;

/**
 *
 * Storage object service, which is used to operate objects in storage,
 * including create, delete, copy, get, put, list objects and so on, This is an
 * interface, which is implemented by different storage clients, and can be the
 * minimal implementation of the storage while manipulating objects.
 *
 */
public interface StorageObjectService<Client> extends StorageClientAware<Client> {

    /**
     * The default content type. According to RFC 2045:
     * <a href="https://datatracker.ietf.org/doc/html/rfc2045">RFC 2045</a>,
     * which refers to following content:
     * <p>
     *    "Any entity with an unrecognized Content-Transfer-Encoding must be
     *    treated as if it has a Content-Type of "application/octet-stream",
     *    regardless of what the Content-Type header field actually says."
     * </p>
     * This configuration could be set in the configuration file. via key:
     * <p>
     * "io.cloudapp.filestore.default.content-type"
	 * </p>
     */
    String DEFAULT_CONTENT_TYPE = "application/octet-stream";

	/**
	 * Creates a copy of an object that is already stored
	 *
     * @param sourceBucket   The source bucket name.
	 * @param sourcePath	 the source name, /path/to/object/name
	 * @param targetBucket   The target bucket name.
	 * @param targetPath 	 the target name, /path/to/object/name
	 * @param override 		 Override it if the object already exists
	 *
	 * @return new object
	 */
	boolean copy(String sourceBucket, String sourcePath,
				 String targetBucket, String targetPath, boolean override)
            throws CloudAppException;

	/**
	 * Delete one single object
	 *
     * @param bucketName  The bucket name.
	 * @param path  the deleting object path, path=/path/to/object/name
	 *
	 * @return  delete result:
	 *                true - deleted
	 *                false - failed to delete
	 */
	boolean deleteObject(String bucketName, String path) throws CloudAppException;

    /**
     * Delete objects
     *
     * @param bucketName  The bucket name.
     * @param objects     Object names waiting to be deleting.
	 * @param checkDeleteAll  If true, Checked if all objects are deleted.
     *
     * @return true if all objects are deleted, else false.
     */
    boolean deleteObjects(String bucketName, Collection<String> objects, boolean checkDeleteAll)
            throws CloudAppException;

    /**
	 * Get object input stream by file path or object name
	 *
     * @param bucketName     The bucket name.
	 * @param path           path=/path/to/object/name
	 *
	 * @return  file information
	 */
	InputStream getObject(String bucketName, String path) throws CloudAppException;


	/**
	 * save object to bucket
	 *
     * @param bucketName     The bucket name.
	 * @param path       file path, path=/path/to/object/name
	 * @param body 		 The uploading body stream. If the body is null,
	 *                   the object will be created with empty content.
	 *
	 * @return object information with objectName
	 */
	default boolean putObject(String bucketName, String path, InputStream body)
            throws CloudAppException {
        return putObject(bucketName, path, body, DEFAULT_CONTENT_TYPE);
    }

    /**
     * save object to bucket
     *
     * @param bucketName     The bucket name.
     * @param path       file path, path=/path/to/object/name
     * @param body 		 The uploading body stream. If the body is null,
     *                   the object will be created with empty content.
     * @param contentType  The content type of the object.
     *
     * @return uploaded result
     */
    boolean putObject(String bucketName, String path, InputStream body,
                      String contentType)
            throws CloudAppException;



    /**
	 * list object versions
	 *
     * @param bucketName     The bucket name.
	 * @param path           The object path, path=/path/to/object/name
	 * @param sinceVersion   The versionId of the object to start listing from.
	 * @param count          The maximum number of versions to return.
	 *
	 * @return A list of object versions.
	 */
	Collection<String> listObjectVersions(String bucketName,
                                          String path,
                                          String sinceVersion,
                                          int count)
            throws CloudAppException;

	/**
	 * list latest 10 object versions
	 *
     * @param bucketName     The bucket name.
	 * @param path  file prefix to list versions with, path=/path/to/object/name
	 *
	 * @return A list of object versions.
	 */
	default Collection<String> listTop10LatestObjectVersions(String bucketName,
                                                             String path)
            throws CloudAppException {
		return listObjectVersions(bucketName, path, null, 10);
	}


	/**
	 * Restore object
	 *
     * @param bucketName   The bucket name.
	 * @param path the restoring object path, path=/path/to/objectName
	 * @param days the restoring days, should be an integer between 1 - 7.
	 * @param tier the restoring tier, tire is one of "Standard", "Bulk",
     *                "Expedited"
	 *
	 * @return true if restore success, else false.
	 */
	boolean restoreObject(String bucketName, String path, int days, String tier)
            throws CloudAppException;
}
