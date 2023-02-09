import Foundation
import Security

@objc public class Attributes: NSObject {
    @objc public class func test() -> Int {
        return 12
    }

    @objc public class func keyAttributes(_ access: SecAccessControl, tag: String) -> CFDictionary {
        return [
            kSecAttrKeyType as String           : kSecAttrKeyTypeEC,
            kSecAttrKeySizeInBits as String     : 256,
            kSecAttrTokenID as String           : kSecAttrTokenIDSecureEnclave,
            kSecPrivateKeyAttrs as String : [
                kSecAttrIsPermanent as String       : true,
                kSecAttrApplicationTag as String    : tag,
                kSecAttrAccessControl as String     : access
            ]
        ] as CFDictionary
    }

    @objc public class func keyQuery(_ tag: String) -> CFDictionary {
        return [
            kSecClass as String                 : kSecClassKey,
            kSecAttrApplicationTag as String    : tag,
            kSecAttrKeyType as String           : kSecAttrKeyTypeEC,
            kSecMatchLimit as String            : kSecMatchLimitOne,
            kSecReturnRef as String             : true
        ] as CFDictionary
    }
}