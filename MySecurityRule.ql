/**
 * @name Tìm lệnh in ra Console có thể lộ dữ liệu
 * @kind problem
 * @id java/custom-print-check
 */
import java
from MethodAccess call
where call.getMethod().hasName("println")
  and call.getArgument(0).toString().toLowerCase().contains("pass")
select call, "Cảnh báo: Không được in biến chứa từ khóa 'pass' ra log!"