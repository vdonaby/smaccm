/* This file has been autogenerated by Ivory
 * Compiler version  0.1.0.0
 */
#include "mavlink_system_time_msg.h"

void mavlink_system_time_msg_send(const struct system_time_msg* n_var0,
                                  uint8_t* n_var1, uint8_t n_var2[80U])
{
    uint8_t n_local0[12U] = {};
    uint8_t* n_ref1 = n_local0;
    uint64_t n_deref2 = n_var0->time_unix_usec;
    
    mavlink_pack_uint64_t((uint8_t*) n_ref1, 0U, n_deref2);
    
    uint32_t n_deref3 = n_var0->time_boot_ms;
    
    mavlink_pack_uint32_t((uint8_t*) n_ref1, 8U, n_deref3);
    for (int32_t n_ix4 = 0; n_ix4 <= 11; n_ix4++) {
        ASSERTS(n_ix4 > 0 && 2147483647 - n_ix4 >= 6 || n_ix4 <= 0);
        if (n_ix4 + 6 >= 80) { } else {
            uint8_t n_deref5 = n_ref1[n_ix4];
            
            ASSERTS(n_ix4 > 0 && 2147483641 >= n_ix4 || n_ix4 <= 0);
            ASSERTS(0 <= 6 + n_ix4 && 6 + n_ix4 < 80);
            *&n_var2[(6 + n_ix4) % 80] = n_deref5;
        }
    }
    mavlinkSendWithWriter(2U, 137U, 12U, n_var1, n_var2);
    for (int32_t n_ix6 = 0; n_ix6 <= 59; n_ix6++) {
        ASSERTS(n_ix6 > 0 && 2147483647 - n_ix6 >= 20 || n_ix6 <= 0);
        ASSERTS(0 <= n_ix6 + 20 && n_ix6 + 20 < 80);
        *&n_var2[(n_ix6 + 20) % 80] = 0U;
    }
    return;
}

void mavlink_system_time_unpack(struct system_time_msg* n_var0, const
                                uint8_t* n_var1)
{
    uint64_t n_r0 = mavlink_unpack_uint64_t(n_var1, 0U);
    
    *&n_var0->time_unix_usec = n_r0;
    
    uint32_t n_r1 = mavlink_unpack_uint32_t(n_var1, 8U);
    
    *&n_var0->time_boot_ms = n_r1;
}