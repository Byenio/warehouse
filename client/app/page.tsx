import { env } from "process";

export default async function Home() {
    const data = await fetch(
        `http://${env.SERVER_ADDRESS}:${env.SERVER_PORT}/api/category`
    );
    const categories: Category[] = await data.json();

    return (
        <div>
            {categories.map((category) => {
                return <div>{category.name}</div>;
            })}
        </div>
    );
}
