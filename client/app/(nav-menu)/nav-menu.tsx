import {
    NavigationMenu,
    NavigationMenuContent,
    NavigationMenuItem,
    NavigationMenuLink,
    NavigationMenuTrigger,
    navigationMenuTriggerStyle,
} from "@/components/ui/navigation-menu";
import Link from "next/link";

export default function NavMenu() {
    return (
        <NavigationMenu>
            <NavigationMenuItem>
                {menuItems.map((item) => (
                    <NavMenuItem item={item} key={item.url} />
                ))}
            </NavigationMenuItem>
        </NavigationMenu>
    );
}

function NavMenuItem({ item }: { item: MenuItem }) {
    if (item.content) {
        return (
            <>
                <NavigationMenuTrigger>{item.name}</NavigationMenuTrigger>
                <NavigationMenuContent className="flex justify-center">
                    {item.content.map((subitem) => (
                        <Link
                            key={subitem.url}
                            href={subitem.url}
                            legacyBehavior
                            passHref
                        >
                            <NavigationMenuLink
                                className={navigationMenuTriggerStyle()}
                            >
                                {subitem.name}
                            </NavigationMenuLink>
                        </Link>
                    ))}
                </NavigationMenuContent>
            </>
        );
    }

    return (
        <>
            <Link href={item.url} legacyBehavior passHref>
                <NavigationMenuLink className={navigationMenuTriggerStyle()}>
                    {item.name}
                </NavigationMenuLink>
            </Link>
        </>
    );
}

type MenuItemContent = {
    name: string;
    url: string;
};

type MenuItem = {
    name: string;
    url: string;
    content?: MenuItemContent[];
};

const menuItems: MenuItem[] = [
    {
        name: "Strona główna",
        url: "/",
    },
    {
        name: "Dodawanie",
        url: "/forms",
        content: [
            {
                name: "Nowy produkt",
                url: "/forms/product",
            },
            {
                name: "Nowy producent",
                url: "/forms/manufacturer",
            },
            {
                name: "Nowa kategoria",
                url: "/forms/category",
            },
            {
                name: "Nowa podkategoria",
                url: "/forms/subcategory",
            },
        ],
    },
    {
        name: "Github",
        url: "https://github.com/byenio/warehouse",
    },
];
