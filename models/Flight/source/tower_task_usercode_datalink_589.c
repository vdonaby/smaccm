/* This file has been autogenerated by Ivory
 * Compiler version  0.1.0.0
 */
#include "tower_task_usercode_datalink_589.h"

struct hxstream_state hx_decoder_state_600 = {.fstate =2U, .offset =0,
                                              .tagSeen =false};

uint32_t airdata_frames_decoded_601 = 0U;

uint8_t airdata_decoded_602[96U];

bool airdata_overrun_603;

uint32_t rs_success_604 = 0U;

uint32_t rs_fail_605 = 0U;

uint8_t rs_msgtype_606;

int32_t rs_len_607;

uint8_t rs_buf_608[48U];

void eventhandler_datalink_589_chan573_609(const uint8_t* n_var0)
{
    uint8_t n_deref0 = *n_var0;
    uint8_t n_deref1 = (&hx_decoder_state_600)->fstate;
    int32_t n_deref2 = (&hx_decoder_state_600)->offset;
    bool n_deref3 = (&hx_decoder_state_600)->tagSeen;
    uint8_t n_r4 = decodeSM(&hx_decoder_state_600, n_deref0);
    uint8_t n_deref5 = (&hx_decoder_state_600)->fstate;
    uint8_t n_deref6 = (&hx_decoder_state_600)->ftag;
    
    if (n_deref1 == 2U && (n_deref5 == 1U && n_deref3)) {
        if (n_deref6 == 0U) {
            bool n_deref7 = *&airdata_overrun_603;
            
            if (n_deref7) { } else {
                emitFromTask_datalink_589_chan586_595(airdata_decoded_602);
                
                uint32_t n_deref8 = *&airdata_frames_decoded_601;
                
                ASSERTS(4294967295U - n_deref8 >= 1U);
                *&airdata_frames_decoded_601 = n_deref8 + 1U;
            }
        }
        if (n_deref6 == 1U) {
            uint8_t n_deref9 = *&rs_msgtype_606;
            int32_t n_deref10 = *&rs_len_607;
            
            if (n_deref9 == 2U && n_deref10 == 0) {
                struct radio_info n_local11 = {};
                struct radio_info* n_ref12 = &n_local11;
                
                emitFromTask_datalink_589_chan588_599(n_ref12);
                
                uint32_t n_deref13 = *&rs_success_604;
                
                ASSERTS(4294967295U - n_deref13 >= 1U);
                *&rs_success_604 = n_deref13 + 1U;
            } else {
                if (n_deref9 == 1U && n_deref10 == 20) {
                    uint8_t n_deref14 = rs_buf_608[1];
                    uint8_t n_deref15 = rs_buf_608[2];
                    uint8_t n_deref16 = rs_buf_608[3];
                    uint8_t n_deref17 = rs_buf_608[4];
                    uint8_t n_deref18 = rs_buf_608[5];
                    uint8_t n_deref19 = rs_buf_608[6];
                    uint8_t n_deref20 = rs_buf_608[7];
                    uint8_t n_deref21 = rs_buf_608[8];
                    uint8_t n_deref22 = rs_buf_608[9];
                    uint8_t n_deref23 = rs_buf_608[10];
                    uint8_t n_deref24 = rs_buf_608[11];
                    uint8_t n_deref25 = rs_buf_608[12];
                    uint8_t n_deref26 = rs_buf_608[13];
                    uint8_t n_deref27 = rs_buf_608[14];
                    uint8_t n_deref28 = rs_buf_608[15];
                    uint8_t n_deref29 = rs_buf_608[16];
                    uint8_t n_deref30 = rs_buf_608[17];
                    uint8_t n_deref31 = rs_buf_608[18];
                    uint8_t n_deref32 = rs_buf_608[19];
                    uint8_t n_deref33 = rs_buf_608[20];
                    struct radio_stat n_local34 = {.sik =true, .loc_rssi =
                                                   n_deref14, .loc_noise =
                                                   n_deref15, .loc_rxctr =
                                                   (uint16_t) n_deref16 * 256U +
                                                   (uint16_t) n_deref17,
                                                   .rem_rssi =n_deref18,
                                                   .rem_noise =n_deref19,
                                                   .rem_rxctr =
                                                   (uint16_t) n_deref20 * 256U +
                                                   (uint16_t) n_deref21,
                                                   .tx_err =
                                                   (uint16_t) n_deref22 * 256U +
                                                   (uint16_t) n_deref23,
                                                   .rx_err =
                                                   (uint16_t) n_deref24 * 256U +
                                                   (uint16_t) n_deref25,
                                                   .tx_ovf =
                                                   (uint16_t) n_deref26 * 256U +
                                                   (uint16_t) n_deref27,
                                                   .rx_ovf =
                                                   (uint16_t) n_deref28 * 256U +
                                                   (uint16_t) n_deref29,
                                                   .ecc_errs =
                                                   (uint16_t) n_deref30 * 256U +
                                                   (uint16_t) n_deref31,
                                                   .ecc_pkts =
                                                   (uint16_t) n_deref32 * 256U +
                                                   (uint16_t) n_deref33};
                    struct radio_stat* n_ref35 = &n_local34;
                    
                    emitFromTask_datalink_589_chan587_597(n_ref35);
                    
                    uint32_t n_deref36 = *&rs_success_604;
                    
                    ASSERTS(4294967295U - n_deref36 >= 1U);
                    *&rs_success_604 = n_deref36 + 1U;
                } else {
                    uint32_t n_deref37 = *&rs_fail_605;
                    
                    ASSERTS(4294967295U - n_deref37 >= 1U);
                    *&rs_fail_605 = n_deref37 + 1U;
                }
            }
        }
    } else {
        if (n_deref1 == 1U) {
            if (n_deref6 == 0U) {
                *&airdata_overrun_603 = false;
                for (int32_t n_ix38 = 0; n_ix38 <= 95; n_ix38++) {
                    *&airdata_decoded_602[n_ix38] = 0U;
                }
            }
            if (n_deref6 == 1U) {
                *&rs_msgtype_606 = 0U;
                *&rs_len_607 = 0;
                for (int32_t n_ix39 = 0; n_ix39 <= 47; n_ix39++) {
                    *&rs_buf_608[n_ix39] = 0U;
                }
            }
        } else {
            if (n_deref1 == 2U || n_deref1 == 3U) {
                if (n_deref6 == 0U) {
                    if (n_deref2 < 96) {
                        ASSERTS(0 <= n_deref2 && n_deref2 < 96);
                        *&airdata_decoded_602[n_deref2 % 96] = n_r4;
                    } else {
                        *&airdata_overrun_603 = true;
                    }
                }
                if (n_deref6 == 1U) {
                    uint8_t n_deref40 = *&rs_msgtype_606;
                    
                    if (n_deref2 >= 48) {
                        *&rs_msgtype_606 = 3U;
                    } else {
                        if (n_deref2 == 0) {
                            if (n_r4 == 66U) {
                                *&rs_msgtype_606 = 1U;
                            } else {
                                if (n_r4 == 65U) {
                                    *&rs_msgtype_606 = 2U;
                                }
                            }
                        } else {
                            if (n_deref40 == 3U) { } else {
                                if (n_deref40 == 1U) {
                                    if (n_deref2 < 22) {
                                        ASSERTS(0 <= n_deref2 && n_deref2 < 48);
                                        *&rs_buf_608[n_deref2 % 48] = n_r4;
                                        *&rs_len_607 = n_deref2;
                                    } else {
                                        *&rs_msgtype_606 = 3U;
                                    }
                                } else {
                                    if (n_deref40 == 2U) {
                                        if (n_deref2 == 1 && n_r4 ==
                                            84U) { } else {
                                            if (n_deref2 == 2 && n_r4 ==
                                                73U) { } else {
                                                if (n_deref2 == 3 && n_r4 ==
                                                    48U) { } else {
                                                    if (n_deref2 == 4 && n_r4 ==
                                                        10U) { } else {
                                                        if (n_deref2 > 4) {
                                                            ASSERTS(n_deref2 <
                                                                0 &&
                                                                2147483643 +
                                                                n_deref2 >= 0 ||
                                                                n_deref2 >= 0);
                                                            ASSERTS(0 <=
                                                                n_deref2 - 4 &&
                                                                n_deref2 - 4 <
                                                                48);
                                                            *&rs_buf_608[(n_deref2 -
                                                                          4) %
                                                                         48] =
                                                                n_r4;
                                                            *&rs_len_607 =
                                                                n_deref2;
                                                        } else {
                                                            *&rs_msgtype_606 =
                                                                3U;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

void eventhandler_datalink_589_chan585_616(const uint8_t n_var0[96U])
{
    uint8_t n_local0 = 126U;
    uint8_t* n_ref1 = &n_local0;
    
    emitFromTask_datalink_589_chan572_611(n_ref1);
    
    uint16_t n_r2 = ivory_hw_io_read_u16(1073762316U);
    
    ivory_hw_io_write_u16(1073762316U, n_r2 & ~(1U << 7U) | (uint16_t) (1U &
                                                                        1U) <<
                          7U);
    
    uint8_t n_local3 = 0U;
    uint8_t* n_ref4 = &n_local3;
    
    emitFromTask_datalink_589_chan572_611(n_ref4);
    
    uint16_t n_r5 = ivory_hw_io_read_u16(1073762316U);
    
    ivory_hw_io_write_u16(1073762316U, n_r5 & ~(1U << 7U) | (uint16_t) (1U &
                                                                        1U) <<
                          7U);
    for (int32_t n_ix6 = 0; n_ix6 <= 95; n_ix6++) {
        uint8_t n_deref7 = n_var0[n_ix6];
        
        if (n_deref7 == 126U || n_deref7 == 124U) {
            uint8_t n_local8 = 124U;
            uint8_t* n_ref9 = &n_local8;
            
            emitFromTask_datalink_589_chan572_611(n_ref9);
            
            uint16_t n_r10 = ivory_hw_io_read_u16(1073762316U);
            
            ivory_hw_io_write_u16(1073762316U, n_r10 & ~(1U << 7U) |
                                  (uint16_t) (1U & 1U) << 7U);
            
            uint8_t n_local11 = 32U ^ n_deref7;
            uint8_t* n_ref12 = &n_local11;
            
            emitFromTask_datalink_589_chan572_611(n_ref12);
            
            uint16_t n_r13 = ivory_hw_io_read_u16(1073762316U);
            
            ivory_hw_io_write_u16(1073762316U, n_r13 & ~(1U << 7U) |
                                  (uint16_t) (1U & 1U) << 7U);
        } else {
            uint8_t n_local14 = n_deref7;
            uint8_t* n_ref15 = &n_local14;
            
            emitFromTask_datalink_589_chan572_611(n_ref15);
            
            uint16_t n_r16 = ivory_hw_io_read_u16(1073762316U);
            
            ivory_hw_io_write_u16(1073762316U, n_r16 & ~(1U << 7U) |
                                  (uint16_t) (1U & 1U) << 7U);
        }
    }
}

void eventhandler_datalink_589_per250_620(const uint32_t* n_var0)
{
    uint32_t n_deref0 = *n_var0;
    uint8_t n_local1[2U] = {66U, 13U};
    uint8_t* n_ref2 = n_local1;
    uint8_t n_local3 = 126U;
    uint8_t* n_ref4 = &n_local3;
    
    emitFromTask_datalink_589_chan572_611(n_ref4);
    
    uint16_t n_r5 = ivory_hw_io_read_u16(1073762316U);
    
    ivory_hw_io_write_u16(1073762316U, n_r5 & ~(1U << 7U) | (uint16_t) (1U &
                                                                        1U) <<
                          7U);
    
    uint8_t n_local6 = 1U;
    uint8_t* n_ref7 = &n_local6;
    
    emitFromTask_datalink_589_chan572_611(n_ref7);
    
    uint16_t n_r8 = ivory_hw_io_read_u16(1073762316U);
    
    ivory_hw_io_write_u16(1073762316U, n_r8 & ~(1U << 7U) | (uint16_t) (1U &
                                                                        1U) <<
                          7U);
    for (int32_t n_ix9 = 0; n_ix9 <= 1; n_ix9++) {
        uint8_t n_deref10 = n_ref2[n_ix9];
        
        if (n_deref10 == 126U || n_deref10 == 124U) {
            uint8_t n_local11 = 124U;
            uint8_t* n_ref12 = &n_local11;
            
            emitFromTask_datalink_589_chan572_611(n_ref12);
            
            uint16_t n_r13 = ivory_hw_io_read_u16(1073762316U);
            
            ivory_hw_io_write_u16(1073762316U, n_r13 & ~(1U << 7U) |
                                  (uint16_t) (1U & 1U) << 7U);
            
            uint8_t n_local14 = 32U ^ n_deref10;
            uint8_t* n_ref15 = &n_local14;
            
            emitFromTask_datalink_589_chan572_611(n_ref15);
            
            uint16_t n_r16 = ivory_hw_io_read_u16(1073762316U);
            
            ivory_hw_io_write_u16(1073762316U, n_r16 & ~(1U << 7U) |
                                  (uint16_t) (1U & 1U) << 7U);
        } else {
            uint8_t n_local17 = n_deref10;
            uint8_t* n_ref18 = &n_local17;
            
            emitFromTask_datalink_589_chan572_611(n_ref18);
            
            uint16_t n_r19 = ivory_hw_io_read_u16(1073762316U);
            
            ivory_hw_io_write_u16(1073762316U, n_r19 & ~(1U << 7U) |
                                  (uint16_t) (1U & 1U) << 7U);
        }
    }
}