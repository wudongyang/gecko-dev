/* -*- Mode: C++; tab-width: 4; indent-tabs-mode: nil; c-basic-offset: 2 -*- */
/* ***** BEGIN LICENSE BLOCK *****
 * Version: NPL 1.1/GPL 2.0/LGPL 2.1
 *
 * The contents of this file are subject to the Netscape Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/NPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 * The Original Code is mozilla.org code.
 *
 * The Initial Developer of the Original Code is 
 * Netscape Communications Corporation.
 * Portions created by the Initial Developer are Copyright (C) 1998
 * the Initial Developer. All Rights Reserved.
 *
 * Contributor(s):
 *
 *
 * Alternatively, the contents of this file may be used under the terms of
 * either the GNU General Public License Version 2 or later (the "GPL"), or
 * the GNU Lesser General Public License Version 2.1 or later (the "LGPL"),
 * in which case the provisions of the GPL or the LGPL are applicable instead
 * of those above. If you wish to allow use of your version of this file only
 * under the terms of either the GPL or the LGPL, and not to allow others to
 * use your version of this file under the terms of the NPL, indicate your
 * decision by deleting the provisions above and replace them with the notice
 * and other provisions required by the GPL or the LGPL. If you do not delete
 * the provisions above, a recipient may use your version of this file under
 * the terms of any one of the NPL, the GPL or the LGPL.
 *
 * ***** END LICENSE BLOCK ***** */

#ifndef DefinesMozilla_h_
#define DefinesMozilla_h_

// *** Security
//#define NADA_VERSION
//#define EXPORT_VERSION
#define US_VERSION

// Cookies/SingleSignon/Wallet
#define CookieManagement 1
#define SingleSignon 1
#define ClientWallet 1

// 98-09-25 mlm - turn on javascript thread safety
#define JS_THREADSAFE 1

// External DTD support for XML
#define XML_DTD

//#define MOZ_PERF_METRICS 1		// Uncomment to get metrics in layout, parser and webshell.
															// You also need to define __TIMESIZE_DOUBLE__ in <timesize.mac.h>

// reflow counters, enabled in debug builds and visible via a pref
#ifdef DEBUG
#define MOZ_REFLOW_PERF 1
#define MOZ_REFLOW_PERF_DSP 1
#endif

// ***************************************************************************
//	�	You typically will not need to change things below here
// ***************************************************************************

#define MOCHA
#define MOZILLA_CLIENT	1
#ifndef NETSCAPE
#define NETSCAPE	1
#endif

#define OJI		1

#ifdef JAVA
	#define UNICODE_FONTLIST 1
#endif

#define NECKO 1

#define MOZILLA_VERSION "1.3b"

#endif /* DefinesMozilla_h_ */
