import UIKit
import SwiftUI
import MobileApp

class SceneDelegate: UIResponder, UIWindowSceneDelegate {
    var window: UIWindow?

    func scene(_ scene: UIScene, willConnectTo session: UISceneSession, options connectionOptions: UIScene.ConnectionOptions) {
        if let windowScene = scene as? UIWindowScene {
            let window = UIWindow(windowScene: windowScene)
            window.rootViewController = EntryPoint.shared.createEntryPoint()
            self.window = window
            window.makeKeyAndVisible()
        }
    }

}
