/* This file has been autogenerated by Ivory
 * Compiler version  0.1.0.2
 */
#ifndef __XYZ_CALIBRATION_TYPES_H__
#define __XYZ_CALIBRATION_TYPES_H__
#ifdef __cplusplus
extern "C" {
#endif
#include "ivory.h"
#include "ivory_serialize.h"
#include "time_micros_types.h"
#include "xyz_types.h"
typedef struct xyz_calibration { bool valid;
                                 float progress;
                                 struct xyz bias;
                                 struct xyz scale;
                                 int64_t time;
} xyz_calibration;
void xyz_calibration_get_le (const uint8_t * n_var0, uint32_t n_var1, struct xyz_calibration * n_var2);
void xyz_calibration_get_be (const uint8_t * n_var0, uint32_t n_var1, struct xyz_calibration * n_var2);
void xyz_calibration_set_le (uint8_t * n_var0, uint32_t n_var1, const struct xyz_calibration * n_var2);
void xyz_calibration_set_be (uint8_t * n_var0, uint32_t n_var1, const struct xyz_calibration * n_var2);

#ifdef __cplusplus
}
#endif
#endif /* __XYZ_CALIBRATION_TYPES_H__ */