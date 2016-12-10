/**
 * Copyright 2016 Acme Rocket Company [acmerocket.com]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.acmerocket.chariot.core;

/**
 * @author philion
 *
 */
public class DeviceException extends RuntimeException { // TODO? or not runtime?
	private static final long serialVersionUID = 6212600228419229538L;
	private final String validInput;
	
	public DeviceException(String message) {
		super(message);
		validInput = null;
	}
	
	public DeviceException(String message, Throwable ex) {
		super(message, ex);
		validInput = null;
	}
	
	public DeviceException(String message, String valid) {
		super(message);
		validInput = valid;
	}
	
	public DeviceException(String message, String valid, Throwable ex) {
		super(message, ex);
		validInput = valid;
	}

	public String getValidInput() {
		return this.validInput;
	}
}
