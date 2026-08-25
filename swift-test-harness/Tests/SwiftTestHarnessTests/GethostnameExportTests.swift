import Testing
import Gethostname

@Test func testSwiftModuleLoads() {
    #expect(!gethostname().isEmpty)
}
