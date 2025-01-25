import { env } from "process";
import CategoryMenu from "./(category-menu)/category-menu";

export default async function Layout({
    children,
}: Readonly<{
    children: React.ReactNode;
}>) {
    const data = await fetch(
        `http://${env.SERVER_ADDRESS}:${env.SERVER_PORT}/api/category`
    );
    const categories: Category[] = await data.json();

    return (
        <div className="flex">
            <div className="basis-1/5">
                <CategoryMenu categories={categories} />
            </div>
            <div className="basis-4/5">{children}</div>
        </div>
    );
}
