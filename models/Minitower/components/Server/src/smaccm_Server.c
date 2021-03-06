/**************************************************************************
  Copyright (c) 2013-2016 Rockwell Collins and the University of
  Minnesota. Developed with the sponsorship of the Defense Advanced
  Research Projects Agency (DARPA).

  Permission is hereby granted, free of charge, to any person
  obtaining a copy of this data, including any software or models
  in source or binary form, as well as any drawings,
  specifications, and documentation (collectively "the Data"), to
  deal in the Data without restriction, including without
  limitation the rights to use, copy, modify, merge, publish,
  distribute, sublicense, and/or sell copies of the Data, and to
  permit persons to whom the Data is furnished to do so, subject to
  the following conditions:

  The above copyright notice and this permission notice shall be
  included in all copies or substantial portions of the Data.

  THE DATA IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
  EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
  OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
  NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS, SPONSORS,
  DEVELOPERS, CONTRIBUTORS, OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
  CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
  CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
  CONNECTION WITH THE DATA OR THE USE OR OTHER DEALINGS IN THE
  DATA.
 **************************************************************************/

/**************************************************************************

  ***AUTOGENERATED CODE: DO NOT MODIFY***

  This C file contains the implementations of the AADL primitives
  used by user-level declarations for thread Server.

  The user code runs in terms of "dispatchers", which cause
  dispatch user-level handlers to execute.  These handlers can
  communicate using the standard AADL primitives, which are mapped
  to C functions.

  The send/receive handlers are not thread safe in CAmkES; it is
  assumed that this is handled by the CAmkES sequentialized access
  to the dispatch handler.  There is only one dispatch interface
  for the component containing all of the dispatch points.

  They are thread safe for eChronos.

  The read/write handlers are thread safe because the writer comes
  through a dispatch interface but the reader is "local" on a dispatch
  interface and so contention may occur.

 **************************************************************************/

#include "smaccm_Server.h"
#include <string.h>
#include <Server.h>

///////////////////////////////////////////////////////////////////////////
//
// Local prototypes for AADL dispatchers
//
///////////////////////////////////////////////////////////////////////////
static void smaccm_Server_periodic_dispatcher_dispatcher(int64_t * periodic_dispatcher);
static void smaccm_Server_Server_initializer_dispatcher(int64_t * Server_initializer);
static void smaccm_Server_decrypt2self_dispatcher(SMACCM_DATA__GIDL decrypt2self);
static void smaccm_Server_framing2self_dispatcher(SMACCM_DATA__GIDL framing2self);




/************************************************************************
 *
 * Static variables and queue management functions for port:
 *     decrypt2self
 *
 ************************************************************************/

static SMACCM_DATA__GIDL smaccm_queue_decrypt2self [1];
static bool smaccm_queue_full_decrypt2self  = false;
static uint32_t smaccm_queue_front_decrypt2self  = 0;
static uint32_t smaccm_queue_back_decrypt2self  = 0;

bool smaccm_queue_is_full_decrypt2self() {
    return (smaccm_queue_front_decrypt2self == smaccm_queue_back_decrypt2self) && (smaccm_queue_full_decrypt2self);
}

bool smaccm_queue_is_empty_decrypt2self() {
    return (smaccm_queue_front_decrypt2self == smaccm_queue_back_decrypt2self) && (!smaccm_queue_full_decrypt2self);
}

bool smaccm_queue_read_decrypt2self(SMACCM_DATA__GIDL decrypt2self) {
    if (smaccm_queue_is_empty_decrypt2self()) {
        return false;
    } else {
        memcpy(decrypt2self, smaccm_queue_decrypt2self[smaccm_queue_back_decrypt2self], sizeof(SMACCM_DATA__GIDL));

        smaccm_queue_back_decrypt2self = (smaccm_queue_back_decrypt2self + 1) % 1;
        smaccm_queue_full_decrypt2self = false ;
        return true;
    }
}

bool smaccm_queue_write_decrypt2self(const SMACCM_DATA__GIDL decrypt2self) {
    if (smaccm_queue_is_full_decrypt2self()) {
        return false;
    } else {
        memcpy(smaccm_queue_decrypt2self[smaccm_queue_front_decrypt2self], decrypt2self, sizeof(SMACCM_DATA__GIDL));

        smaccm_queue_front_decrypt2self = (smaccm_queue_front_decrypt2self + 1) % 1;
        if (smaccm_queue_back_decrypt2self == smaccm_queue_front_decrypt2self) {
            smaccm_queue_full_decrypt2self = true ;
        }
        return true;
    }
}

/************************************************************************
 *  decrypt2self_write_SMACCM_DATA__GIDL:
 * Invoked by: remote interface.
 *
 * This is the function invoked by a remote RPC to write to an active-thread
 * input event data port.  It queues the input message into a circular buffer.
 *
 ************************************************************************/

bool decrypt2self_write_SMACCM_DATA__GIDL(const smaccm_SMACCM_DATA__GIDL_container * arg) {
    bool result;
    smaccm_server_decrypt2self_mut_lock();
    result = smaccm_queue_write_decrypt2self((uint8_t *) arg);
    smaccm_server_decrypt2self_mut_unlock(); 
    return result;
}


/************************************************************************
 *  Server_read_decrypt2self:
 * Invoked from local active thread.
 *
 * This is the function invoked by the active thread to read from the
 * input event data queue circular buffer.
 *
 ************************************************************************/

bool Server_read_decrypt2self(SMACCM_DATA__GIDL arg) {
    bool result;
    smaccm_server_decrypt2self_mut_lock();
    result = smaccm_queue_read_decrypt2self(arg);
    smaccm_server_decrypt2self_mut_unlock(); 
    return result;
}


/************************************************************************
 *
 * Static variables and queue management functions for port:
 *     framing2self
 *
 ************************************************************************/

static SMACCM_DATA__GIDL smaccm_queue_framing2self [1];
static bool smaccm_queue_full_framing2self  = false;
static uint32_t smaccm_queue_front_framing2self  = 0;
static uint32_t smaccm_queue_back_framing2self  = 0;

bool smaccm_queue_is_full_framing2self() {
    return (smaccm_queue_front_framing2self == smaccm_queue_back_framing2self) && (smaccm_queue_full_framing2self);
}

bool smaccm_queue_is_empty_framing2self() {
    return (smaccm_queue_front_framing2self == smaccm_queue_back_framing2self) && (!smaccm_queue_full_framing2self);
}

bool smaccm_queue_read_framing2self(SMACCM_DATA__GIDL framing2self) {
    if (smaccm_queue_is_empty_framing2self()) {
        return false;
    } else {
        memcpy(framing2self, smaccm_queue_framing2self[smaccm_queue_back_framing2self], sizeof(SMACCM_DATA__GIDL));

        smaccm_queue_back_framing2self = (smaccm_queue_back_framing2self + 1) % 1;
        smaccm_queue_full_framing2self = false ;
        return true;
    }
}

bool smaccm_queue_write_framing2self(const SMACCM_DATA__GIDL framing2self) {
    if (smaccm_queue_is_full_framing2self()) {
        return false;
    } else {
        memcpy(smaccm_queue_framing2self[smaccm_queue_front_framing2self], framing2self, sizeof(SMACCM_DATA__GIDL));

        smaccm_queue_front_framing2self = (smaccm_queue_front_framing2self + 1) % 1;
        if (smaccm_queue_back_framing2self == smaccm_queue_front_framing2self) {
            smaccm_queue_full_framing2self = true ;
        }
        return true;
    }
}

/************************************************************************
 *  framing2self_write_SMACCM_DATA__GIDL:
 * Invoked by: remote interface.
 *
 * This is the function invoked by a remote RPC to write to an active-thread
 * input event data port.  It queues the input message into a circular buffer.
 *
 ************************************************************************/

bool framing2self_write_SMACCM_DATA__GIDL(const smaccm_SMACCM_DATA__GIDL_container * arg) {
    bool result;
    smaccm_server_framing2self_mut_lock();
    result = smaccm_queue_write_framing2self((uint8_t *) arg);
    smaccm_server_framing2self_mut_unlock(); 
    return result;
}


/************************************************************************
 *  Server_read_framing2self:
 * Invoked from local active thread.
 *
 * This is the function invoked by the active thread to read from the
 * input event data queue circular buffer.
 *
 ************************************************************************/

bool Server_read_framing2self(SMACCM_DATA__GIDL arg) {
    bool result;
    smaccm_server_framing2self_mut_lock();
    result = smaccm_queue_read_framing2self(arg);
    smaccm_server_framing2self_mut_unlock(); 
    return result;
}



// NOT writing dispatch variables for port self2encrypt
// NOT writing dispatch variables for port self2framing
// NOT writing dispatch variables for port self2vm_reboot


/************************************************************************
 * periodic_dispatcher Declarations
 *
 ************************************************************************/

static bool smaccm_occurred_periodic_dispatcher;
static int64_t smaccm_time_periodic_dispatcher;

/************************************************************************
 * Server_periodic_dispatcher_write_int64_t
 * Invoked from remote periodic dispatch thread.
 *
 * This function records the current time and triggers the active thread
 * dispatch from a periodic event.  Note that the periodic dispatch
 * thread is the *only* thread that triggers a dispatch, so we do not
 * mutex lock the function.
 *
 ************************************************************************/

bool Server_periodic_dispatcher_write_int64_t(const int64_t * arg) {
    smaccm_occurred_periodic_dispatcher = true;
    smaccm_time_periodic_dispatcher = *arg;
    smaccm_dispatch_sem_post();

    return true;
}

 
 
 
/************************************************************************
 *  dispatch_dispatch_periodic_dispatcher:
 * Invoked by remote RPC (or, for active thread, local dispatcher).
 *
 * This is the function invoked by an active thread dispatcher to
 * call to a user-defined entrypoint function.  It sets up the dispatch
 * context for the user-defined entrypoint, then calls it.
 *
 ************************************************************************/
void dispatch_dispatch_periodic_dispatcher(
  const int64_t * in_arg ,
  smaccm_Server_periodic_dispatcher_struct *out_arg) {
     
    component_entry( in_arg);

     
}

/************************************************************************
 *  dispatch_dispatch_Server_initializer:
 * Invoked by remote RPC (or, for active thread, local dispatcher).
 *
 * This is the function invoked by an active thread dispatcher to
 * call to a user-defined entrypoint function.  It sets up the dispatch
 * context for the user-defined entrypoint, then calls it.
 *
 ************************************************************************/
void dispatch_dispatch_Server_initializer(
  const int64_t * in_arg ,
  smaccm_Server_Server_initializer_struct *out_arg) {
     
    component_init( in_arg);

     
}

/************************************************************************
 *  dispatch_dispatch_decrypt2self:
 * Invoked by remote RPC (or, for active thread, local dispatcher).
 *
 * This is the function invoked by an active thread dispatcher to
 * call to a user-defined entrypoint function.  It sets up the dispatch
 * context for the user-defined entrypoint, then calls it.
 *
 ************************************************************************/
void dispatch_dispatch_decrypt2self(
  const smaccm_SMACCM_DATA__GIDL_container * in_arg ,
  smaccm_Server_decrypt2self_struct *out_arg) {
     
     
}

/************************************************************************
 *  dispatch_dispatch_framing2self:
 * Invoked by remote RPC (or, for active thread, local dispatcher).
 *
 * This is the function invoked by an active thread dispatcher to
 * call to a user-defined entrypoint function.  It sets up the dispatch
 * context for the user-defined entrypoint, then calls it.
 *
 ************************************************************************/
void dispatch_dispatch_framing2self(
  const smaccm_SMACCM_DATA__GIDL_container * in_arg ,
  smaccm_Server_framing2self_struct *out_arg) {
     
     
}


/************************************************************************
 * Server_write_self2encrypt
 * Invoked from local active or passive thread.
 *
 * This is the comm function invoked by a user-level thread to send a message
 * to another thread.  If the target is an active thread, it calls an
 * RPC on the target thread to queue the data.  If it is a passive thread,
 * it locally enqueues the request to be sent when the user thread
 * completes execution.
 *
 ************************************************************************/

bool Server_write_self2encrypt(const SMACCM_DATA__GIDL self2encrypt) {
    bool result = true;
    result &= Server_self2encrypt_write_SMACCM_DATA__GIDL(self2encrypt);


    return result;
}
/************************************************************************
 * Server_write_self2framing
 * Invoked from local active or passive thread.
 *
 * This is the comm function invoked by a user-level thread to send a message
 * to another thread.  If the target is an active thread, it calls an
 * RPC on the target thread to queue the data.  If it is a passive thread,
 * it locally enqueues the request to be sent when the user thread
 * completes execution.
 *
 ************************************************************************/

bool Server_write_self2framing(const SMACCM_DATA__GIDL self2framing) {
    bool result = true;
    result &= Server_self2framing_write_SMACCM_DATA__GIDL(self2framing);


    return result;
}
/************************************************************************
 * Server_write_self2vm_reboot
 * Invoked from local active or passive thread.
 *
 * This is the comm function invoked by a user-level thread to send a message
 * to another thread.  If the target is an active thread, it calls an
 * RPC on the target thread to queue the data.  If it is a passive thread,
 * it locally enqueues the request to be sent when the user thread
 * completes execution.
 *
 ************************************************************************/

bool Server_write_self2vm_reboot(const bool * self2vm_reboot) {
    bool result = true;
    result &= Server_self2vm_reboot_write_bool(self2vm_reboot);


    return result;
}

SMACCM_DATA__Camera_Bounding_Box_i smaccm_vm2self_var ;

/************************************************************************
 *  Server_read_vm2self:
 * Invoked from local active thread.
 *
 * This is the function invoked by the active thread to read from a data port.
 *
 ************************************************************************/

bool Server_read_vm2self(SMACCM_DATA__Camera_Bounding_Box_i * vm2self) {
    smaccm_server_vm2self_mut_lock();
    memcpy(vm2self, &smaccm_vm2self_var, sizeof(SMACCM_DATA__Camera_Bounding_Box_i));
    smaccm_server_vm2self_mut_unlock(); 
    return true;
}

/************************************************************************
 *  vm2self_write_SMACCM_DATA__Camera_Bounding_Box_i:
 * Invoked by remote RPC.
 *
 * This is the function that is invoked by an RPC to write to a data port.
 *
 ************************************************************************/

bool vm2self_write_SMACCM_DATA__Camera_Bounding_Box_i(const SMACCM_DATA__Camera_Bounding_Box_i * vm2self) {
    smaccm_server_vm2self_mut_lock();
    memcpy(&smaccm_vm2self_var, vm2self, sizeof(SMACCM_DATA__Camera_Bounding_Box_i));
    smaccm_server_vm2self_mut_unlock(); 
    return true;
}


/************************************************************************
 * smaccm_Server_periodic_dispatcher_dispatcher
 * Invoked from local active thread.
 *
 * This is the dispatcher function invoked to respond to an incoming thread
 * stimulus: an ISR, a periodic dispatch, or a queued event.
 *
 ******************************************************************************/
static smaccm_Server_periodic_dispatcher_struct smaccm_Server_periodic_dispatcher_arg;

static void smaccm_Server_periodic_dispatcher_dispatcher(int64_t * periodic_dispatcher) {
    // make the call:
    dispatch_dispatch_periodic_dispatcher(
        periodic_dispatcher,
        &smaccm_Server_periodic_dispatcher_arg
    );
}

/************************************************************************
 * smaccm_Server_Server_initializer_dispatcher
 * Invoked from local active thread.
 *
 * This is the dispatcher function invoked to respond to an incoming thread
 * stimulus: an ISR, a periodic dispatch, or a queued event.
 *
 ******************************************************************************/
static smaccm_Server_Server_initializer_struct smaccm_Server_Server_initializer_arg;

static void smaccm_Server_Server_initializer_dispatcher(int64_t * Server_initializer) {
    // make the call:
    dispatch_dispatch_Server_initializer(
        Server_initializer,
        &smaccm_Server_Server_initializer_arg
    );
}

/************************************************************************
 * smaccm_Server_decrypt2self_dispatcher
 * Invoked from local active thread.
 *
 * This is the dispatcher function invoked to respond to an incoming thread
 * stimulus: an ISR, a periodic dispatch, or a queued event.
 *
 ******************************************************************************/
static smaccm_Server_decrypt2self_struct smaccm_Server_decrypt2self_arg;

static void smaccm_Server_decrypt2self_dispatcher(SMACCM_DATA__GIDL decrypt2self) {
    // make the call:
    dispatch_dispatch_decrypt2self(
        (smaccm_SMACCM_DATA__GIDL_container *) decrypt2self,
        &smaccm_Server_decrypt2self_arg
    );
}

/************************************************************************
 * smaccm_Server_framing2self_dispatcher
 * Invoked from local active thread.
 *
 * This is the dispatcher function invoked to respond to an incoming thread
 * stimulus: an ISR, a periodic dispatch, or a queued event.
 *
 ******************************************************************************/
static smaccm_Server_framing2self_struct smaccm_Server_framing2self_arg;

static void smaccm_Server_framing2self_dispatcher(SMACCM_DATA__GIDL framing2self) {
    // make the call:
    dispatch_dispatch_framing2self(
        (smaccm_SMACCM_DATA__GIDL_container *) framing2self,
        &smaccm_Server_framing2self_arg
    );
}


/************************************************************************
 * int run(void)
 * Main active thread function.
 *
 ************************************************************************/

int run(void) {
    // thread initialization routines (if any)...
    int64_t dummy_time = 0;
    smaccm_Server_Server_initializer_dispatcher(&dummy_time);


    // Initial lock to await dispatch input.
    smaccm_dispatch_sem_wait();

    for(;;) {
        smaccm_dispatch_sem_wait();


        // Drain the queues
        if (smaccm_occurred_periodic_dispatcher) {
            smaccm_occurred_periodic_dispatcher = false;
            smaccm_Server_periodic_dispatcher_dispatcher(&smaccm_time_periodic_dispatcher);
        }


    }

    // Won't ever get here, but form must be followed
    return 0;
}

