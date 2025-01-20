import { env } from "process";
import ProductCard from "./product-card";

export default async function Products() {
    const data = await fetch(
        `http://${env.SERVER_ADDRESS}:${env.SERVER_PORT}/api/product`
    );
    const products: Product[] = await data.json();

    return (
        <div className="flex flex-wrap m-10 justify-evenly">
            {products.map((product) => (
                <ProductCard key={product.id} product={product} />
            ))}
        </div>
    );
}
