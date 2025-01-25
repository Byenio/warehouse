import {
    NavigationMenu,
    NavigationMenuItem,
    NavigationMenuLink,
    navigationMenuTriggerStyle,
} from "@/components/ui/navigation-menu";
import Link from "next/link";

export default function NavMenu() {
    return (
        <NavigationMenu>
            <NavigationMenuItem>
                {menuItems.map((item) => (
                    <Link
                        key={item.url}
                        href={item.url}
                        legacyBehavior
                        passHref
                    >
                        <NavigationMenuLink
                            className={navigationMenuTriggerStyle()}
                        >
                            {item.name}
                        </NavigationMenuLink>
                    </Link>
                ))}
            </NavigationMenuItem>
        </NavigationMenu>
    );
}

const menuItems = [
    {
        name: "Strona główna",
        url: "/",
    },
    {
        name: "Dodawanie",
        url: "/forms",
    },
    {
        name: "Github",
        url: "https://github.com/byenio/warehouse",
    },
];
