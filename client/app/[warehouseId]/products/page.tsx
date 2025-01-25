import { env } from "process";
import ProductCard from "./product-card";

export default async function Products({
    params,
}: {
    params: Promise<{ warehouseId: string }>;
}) {
    const { warehouseId } = await params;

    const data = await fetch(
        `http://${env.SERVER_ADDRESS}:${env.SERVER_PORT}/api/product`,
        { cache: "force-cache" }
    );
    const allProducts: Product[] = await data.json();

    const products: Product[] = allProducts.filter(
        (product) => product.warehouseId === warehouseId
    );

    return (
        <div className="grid grid-cols-3 m-4 justify-evenly">
            {products.map((product) => (
                <ProductCard key={product.id} product={product} />
            ))}
        </div>
    );
}
