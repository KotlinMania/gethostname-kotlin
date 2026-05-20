// port-lint: ignore KMP public facade for the src/lib.rs platform implementations
package io.github.kotlinmania.gethostname

// Copyright Sebastian Wiesner <sebastian@swsnr.de>

// Licensed under the Apache License, Version 2.0 (the "License"); you may not
// use this file except in compliance with the License. You may obtain a copy of
// the License at

// 	http://www.apache.org/licenses/LICENSE-2.0

// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
// WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
// License for the specific language governing permissions and limitations under
// the License.

/**
 * [gethostname()][ghn] for all platforms.
 *
 * ```kotlin
 * import io.github.kotlinmania.gethostname.gethostname
 *
 * println("Hostname: ${gethostname()}")
 * ```
 *
 * [ghn]: http://pubs.opengroup.org/onlinepubs/9699919799/functions/gethostname.html
 */

/**
 * Get the standard host name for the current machine.
 *
 * On Unix call the platform's `uname()` to obtain the node name; this matches the node name
 * field semantics from the POSIX `utsname` structure.
 *
 * On Windows return the DNS host name of the local computer, as returned by
 * [GetComputerNameExW] with `ComputerNamePhysicalDnsHostname` as `NameType`. We call this function
 * twice to obtain the appropriate buffer size; there is a race condition window between these two
 * calls where a change to the node name would result in a wrong buffer size which could cause this
 * function to panic.
 *
 * Note that this host name does not have a well-defined meaning in terms of network name
 * resolution. Specifically, it is not guaranteed that the returned name can be resolved in any
 * particular way, e.g. DNS.
 *
 * [GetComputerNameExW]: https://docs.microsoft.com/en-us/windows/desktop/api/sysinfoapi/nf-sysinfoapi-getcomputernameexw
 */
public expect fun gethostname(): String
