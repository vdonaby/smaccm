/* This file has been autogenerated by Ivory
 * Compiler version  0.1.0.0
 */
#ifndef __USERINPUT_TYPE_H__
#define __USERINPUT_TYPE_H__
#ifdef __cplusplus
extern "C" {
#endif
#include "ivory.h"
#include "mavlink_rc_channels_override_msg.h"
struct userinput_result {
    float throttle;
    float roll;
    float pitch;
    float yaw;
    uint32_t time;
} __attribute__((__packed__));

#ifdef __cplusplus
}
#endif
#endif /* __USERINPUT_TYPE_H__ */